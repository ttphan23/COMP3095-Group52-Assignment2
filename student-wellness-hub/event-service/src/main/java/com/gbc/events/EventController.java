package com.gbc.events;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService service;
    private final EventRepository repository;

    public EventController(EventService service, EventRepository repository) {
        this.service = service;
        this.repository = repository;
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
}
