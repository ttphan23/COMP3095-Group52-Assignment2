package com.gbc.events;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EventService {

    private final EventRepository repository;
    private final RestTemplate restTemplate;

    public EventService(EventRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public List<Event> getAllEvents() {
        return repository.findAll();
    }

    public Event createEvent(Event event) {
        return repository.save(event);
    }

    public Event updateEvent(Long id, Event updated) {
        return repository.findById(id)
                .map(event -> {
                    event.setTitle(updated.getTitle());
                    event.setDescription(updated.getDescription());
                    event.setDate(updated.getDate());
                    event.setLocation(updated.getLocation());
                    event.setCapacity(updated.getCapacity());
                    event.setCategory(updated.getCategory());
                    return repository.save(event);
                })
                .orElseThrow(() -> new RuntimeException("Event not found: " + id));
    }

    public void deleteEvent(Long id) {
        repository.deleteById(id);
    }

    public Event registerStudent(Long id, String email) {
        Event e = repository.findById(id).orElseThrow();
        e.getRegisteredStudents().add(email);
        return repository.save(e);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getResourcesForEvent(Event event) {
        String url = "http://wellness-resource-service:8081/resources/category/" + event.getCategory();
        return restTemplate.getForObject(url, List.class);
    }
}
