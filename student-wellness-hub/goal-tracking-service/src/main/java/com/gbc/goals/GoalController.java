package com.gbc.goals;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goals")
public class GoalController {

    private final GoalService service;
    private final GoalRepository repository;

    public GoalController(GoalService service, GoalRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @GetMapping
    public List<Goal> getAllGoals() {
        return service.getAllGoals();
    }

    @PostMapping
    public Goal createGoal(@RequestBody Goal goal) {
        return service.createGoal(goal);
    }

    @PutMapping("/{id}")
    public Goal updateGoal(@PathVariable String id, @RequestBody Goal goal) {
        return service.updateGoal(id, goal);
    }

    @DeleteMapping("/{id}")
    public void deleteGoal(@PathVariable String id) {
        service.deleteGoal(id);
    }

    @GetMapping("/category/{category}")
    public List<Goal> getGoalsByCategory(@PathVariable String category) {
        return service.findByCategory(category);
    }

    @GetMapping("/status/{status}")
    public List<Goal> getGoalsByStatus(@PathVariable String status) {
        return service.findByStatus(status);
    }

    @PatchMapping("/{id}/complete")
    public Goal markGoalCompleted(@PathVariable String id) {
        return service.markCompleted(id);
    }

    @GetMapping("/{id}/resources")
    public ResponseEntity<?> getResourcesForGoal(@PathVariable String id) {
        Goal goal = repository.findById(id).orElse(null);
        if (goal == null) {
            return ResponseEntity.notFound().build();
        }
        List<Map<String, Object>> resources = service.getResourcesForGoal(goal);
        return ResponseEntity.ok(resources);
    }
}
