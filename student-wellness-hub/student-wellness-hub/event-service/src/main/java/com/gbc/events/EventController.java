package com.gbc.events;

import com.gbc.events.kafka.GoalCompletedConsumer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;
    private final EventRepository repository;
    private final GoalCompletedConsumer consumer;

    public EventController(EventService service,
                           EventRepository repository,
                           GoalCompletedConsumer consumer) {
        this.service = service;
        this.repository = repository;
        this.consumer = consumer;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return service.getAllEvents();
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return service.createEvent(event);
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
        return service.updateEvent(id, event);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
    }

    @GetMapping("/{id}/resources")
    public ResponseEntity<?> getResourcesForEvent(@PathVariable Long id) {
        Event event = repository.findById(id).orElse(null);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        List<Map<String, Object>> resources = service.getResourcesForEvent(event);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<Event> register(
            @PathVariable Long id,
            @RequestParam String studentEmail) {
        return ResponseEntity.ok(service.registerStudent(id, studentEmail));
    }

    // -------------------------
    // NEW: Kafka recommendations
    // -------------------------
    @GetMapping("/recommendations/{goalId}")
    public List<Event> recommendationsForGoal(@PathVariable String goalId) {
        return consumer.getRecommendations(goalId);
    }
}
