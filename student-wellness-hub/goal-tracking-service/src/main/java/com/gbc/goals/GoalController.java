package com.gbc.goals;

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
@RequestMapping("/goals")
@Tag(name = "Goals", description = "Wellness Goal Tracking API")
public class GoalController {

    private final GoalService service;
    private final GoalRepository repository;

    public GoalController(GoalService service, GoalRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Operation(summary = "Get all goals", description = "Retrieves a list of all wellness goals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all goals",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Goal.class))))
    })
    @GetMapping
    public List<Goal> getAllGoals() {
        return service.getAllGoals();
    }

    @Operation(summary = "Create a new goal", description = "Creates a new wellness goal (Student only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Goal created successfully",
                    content = @Content(schema = @Schema(implementation = Goal.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied - Student role required", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Goal createGoal(
            @Parameter(description = "Goal to create", required = true)
            @RequestBody Goal goal) {
        return service.createGoal(goal);
    }

    @Operation(summary = "Update a goal", description = "Updates an existing wellness goal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Goal updated successfully",
                    content = @Content(schema = @Schema(implementation = Goal.class))),
            @ApiResponse(responseCode = "404", description = "Goal not found", content = @Content)
    })
    @PutMapping("/{id}")
    public Goal updateGoal(
            @Parameter(description = "Goal ID", required = true)
            @PathVariable String id,
            @Parameter(description = "Updated goal data", required = true)
            @RequestBody Goal goal) {
        return service.updateGoal(id, goal);
    }

    @Operation(summary = "Delete a goal", description = "Deletes a wellness goal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Goal deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Goal not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGoal(
            @Parameter(description = "Goal ID", required = true)
            @PathVariable String id) {
        service.deleteGoal(id);
    }

    @Operation(summary = "Get goals by category", description = "Retrieves all wellness goals in a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved goals by category",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Goal.class))))
    })
    @GetMapping("/category/{category}")
    public List<Goal> getGoalsByCategory(
            @Parameter(description = "Category name (e.g., fitness, mindfulness, nutrition)", required = true)
            @PathVariable String category) {
        return service.findByCategory(category);
    }

    @Operation(summary = "Get goals by status", description = "Retrieves all wellness goals with a specific status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved goals by status",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Goal.class))))
    })
    @GetMapping("/status/{status}")
    public List<Goal> getGoalsByStatus(
            @Parameter(description = "Status (e.g., in-progress, completed)", required = true)
            @PathVariable String status) {
        return service.findByStatus(status);
    }

    @Operation(summary = "Mark goal as completed", 
               description = "Marks a wellness goal as completed and publishes a Kafka event for recommendations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Goal marked as completed successfully",
                    content = @Content(schema = @Schema(implementation = Goal.class))),
            @ApiResponse(responseCode = "404", description = "Goal not found", content = @Content)
    })
    @PatchMapping("/{id}/complete")
    public Goal markGoalCompleted(
            @Parameter(description = "Goal ID", required = true)
            @PathVariable String id) {
        return service.markCompleted(id);
    }

    @Operation(summary = "Get resources for a goal", description = "Retrieves wellness resources related to a goal's category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved resources"),
            @ApiResponse(responseCode = "404", description = "Goal not found", content = @Content)
    })
    @GetMapping("/{id}/resources")
    public ResponseEntity<?> getResourcesForGoal(
            @Parameter(description = "Goal ID", required = true)
            @PathVariable String id) {
        Goal goal = repository.findById(id).orElse(null);
        if (goal == null) {
            return ResponseEntity.notFound().build();
        }
        List<Map<String, Object>> resources = service.getResourcesForGoal(goal);
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get goal by ID", description = "Retrieves a specific wellness goal by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the goal",
                    content = @Content(schema = @Schema(implementation = Goal.class))),
            @ApiResponse(responseCode = "404", description = "Goal not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Goal> getGoalById(
            @Parameter(description = "Goal ID", required = true)
            @PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
