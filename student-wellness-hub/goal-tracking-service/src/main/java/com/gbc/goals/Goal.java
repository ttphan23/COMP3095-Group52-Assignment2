package com.gbc.goals;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "goals")
@Schema(description = "Wellness Goal entity representing student wellness objectives")
public class Goal {

    @Id
    @Schema(description = "Unique identifier of the goal", example = "507f1f77bcf86cd799439011", accessMode = Schema.AccessMode.READ_ONLY)
    private String goalId;

    @Schema(description = "Title of the wellness goal", example = "Complete 30-day meditation challenge", required = true)
    private String title;

    @Schema(description = "Detailed description of the goal", example = "Practice mindfulness meditation for at least 10 minutes every day for 30 days")
    private String description;

    @Schema(description = "Target date for goal completion", example = "2025-02-01", required = true)
    private LocalDate targetDate;

    @Schema(description = "Current status of the goal", example = "in-progress", 
            allowableValues = {"in-progress", "completed", "cancelled"})
    private String status;

    @Schema(description = "Category of the goal", example = "mindfulness", 
            allowableValues = {"fitness", "mindfulness", "nutrition", "mental-health", "sleep"})
    private String category;

    // Constructors
    public Goal() {}

    public Goal(String title, String description, LocalDate targetDate, String status, String category) {
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.status = status;
        this.category = category;
    }

    // Getters & Setters
    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
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

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
