package com.gbc.wellness.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "resources")
@Schema(description = "Wellness Resource entity representing educational content and materials")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the resource", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long resourceId;

    @Schema(description = "Title of the wellness resource", example = "Introduction to Mindfulness", required = true)
    private String title;

    @Schema(description = "Detailed description of the resource", example = "A comprehensive guide to mindfulness meditation techniques")
    private String description;

    @Schema(description = "Category of the resource", example = "mindfulness", 
            allowableValues = {"fitness", "mindfulness", "nutrition", "mental-health", "sleep"})
    private String category;

    @Schema(description = "URL link to the resource content", example = "https://wellness.gbc.ca/resources/mindfulness-guide")
    private String url;

    // Getters and setters
    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
