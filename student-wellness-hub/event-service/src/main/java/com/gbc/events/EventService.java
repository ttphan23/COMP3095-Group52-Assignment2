package com.gbc.events;

import com.gbc.events.dto.ResourceDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;

@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

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

    // Resilience4j – CircuitBreaker + Retry + RateLimiter
    @CircuitBreaker(name = "resourceService", fallbackMethod = "getResourcesForEventFallback")
    @Retry(name = "resourceService")
    @RateLimiter(name = "resourceService")
    public List<ResourceDto> getResourcesForEvent(Long eventId) {

        log.info(">>> getResourcesForEvent called, eventId={}", eventId);

        Event event = repository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        String category = event.getCategory();
        String url = "http://wellness-resource-service:8081/resources/category/" + category;

        try {
            ResourceDto[] response = restTemplate.getForObject(url, ResourceDto[].class);

            if (response == null) {
                return List.of();
            }

            return Arrays.asList(response);

        } catch (Exception ex) {

            return getResourcesForEventFallback(eventId, ex);
        }
    }

    public List<ResourceDto> getResourcesForEventFallback(Long eventId, Throwable t) {
        log.warn(">>> [Fallback] eventId={}, reason={} : {}",
                eventId, t.getClass().getSimpleName(), t.getMessage());

        return List.of();
    }
}
