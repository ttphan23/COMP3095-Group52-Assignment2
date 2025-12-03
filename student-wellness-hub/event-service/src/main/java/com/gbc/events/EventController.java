package com.gbc.events;

import com.gbc.events.kafka.GoalCompletedConsumer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/events")
@Tag(name = "Events", description = "Wellness Event Management API")
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

    @Operation(summary = "Get all events", description = "Retrieves a list of all wellness events")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all events",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Event.class))))
    })
    @GetMapping
    public List<Event> getAllEvents() {
        return service.getAllEvents();
    }

    @Operation(summary = "Create a new event", description = "Creates a new wellness event (Staff only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully",
                    content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied - Staff role required", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(
            @Parameter(description = "Event to create", required = true)
            @RequestBody Event event) {
        return service.createEvent(event);
    }

    @Operation(summary = "Update an event", description = "Updates an existing wellness event (Staff only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully",
                    content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied - Staff role required", content = @Content)
    })
    @PutMapping("/{id}")
    public Event updateEvent(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated event data", required = true)
            @RequestBody Event event) {
        return service.updateEvent(id, event);
    }

    @Operation(summary = "Delete an event", description = "Deletes a wellness event (Staff only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied - Staff role required", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long id) {
        service.deleteEvent(id);
    }

    @Operation(summary = "Get resources for an event", description = "Retrieves wellness resources related to an event's category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved resources"),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @GetMapping("/{id}/resources")
    public ResponseEntity<?> getResourcesForEvent(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long id) {
        Event event = repository.findById(id).orElse(null);
        if (event == null) {
            return ResponseEntity.notFound().build();
        }
        List<Map<String, Object>> resources = service.getResourcesForEvent(event);
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get event by ID", description = "Retrieves a specific wellness event by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the event",
                    content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Register for an event", description = "Registers a student for a wellness event (Student only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered for the event",
                    content = @Content(schema = @Schema(implementation = Event.class))),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied - Student role required", content = @Content)
    })
    @PostMapping("/{id}/register")
    public ResponseEntity<Event> register(
            @Parameter(description = "Event ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Student email address", required = true)
            @RequestParam String studentEmail) {
        return ResponseEntity.ok(service.registerStudent(id, studentEmail));
    }

    @Operation(summary = "Get event recommendations for a goal", 
               description = "Retrieves recommended events based on a completed goal (via Kafka)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recommendations",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Event.class))))
    })
    @GetMapping("/recommendations/{goalId}")
    public List<Event> recommendationsForGoal(
            @Parameter(description = "Goal ID", required = true)
            @PathVariable String goalId) {
        return consumer.getRecommendations(goalId);
    }
}
