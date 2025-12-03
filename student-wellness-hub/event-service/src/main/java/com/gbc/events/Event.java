package com.gbc.events;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "events")
@Schema(description = "Wellness Event entity representing scheduled wellness activities")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the event", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long eventId;

    @Schema(description = "Title of the wellness event", example = "Morning Yoga Session", required = true)
    private String title;

    @Schema(description = "Detailed description of the event", example = "A relaxing yoga session for beginners")
    private String description;

    @Schema(description = "Date of the event", example = "2025-01-15", required = true)
    private LocalDate date;

    @Schema(description = "Location where the event will be held", example = "GBC Student Centre Room 201")
    private String location;

    @Schema(description = "Maximum number of participants", example = "30")
    private int capacity;

    @Schema(description = "Category of the event", example = "fitness", 
            allowableValues = {"fitness", "mindfulness", "nutrition", "mental-health", "social"})
    private String category;

    @ElementCollection
    @JsonIgnore
    @Schema(description = "Set of registered student emails", accessMode = Schema.AccessMode.READ_ONLY)
    private Set<String> registeredStudents = new HashSet<>();

    // Getters and setters
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Set<String> getRegisteredStudents() { return registeredStudents; }
    public void setRegisteredStudents(Set<String> registeredStudents) { this.registeredStudents = registeredStudents; }
}
