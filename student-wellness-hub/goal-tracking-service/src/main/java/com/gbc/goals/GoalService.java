package com.gbc.goals;

import com.gbc.goals.kafka.GoalCompletedProducer;
import com.gbc.schemas.GoalCompletedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class GoalService {

    private final GoalRepository repository;
    private final RestTemplate restTemplate;
    private final GoalCompletedProducer producer;

    public GoalService(GoalRepository repository,
                       RestTemplate restTemplate,
                       GoalCompletedProducer producer) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.producer = producer;
    }

    public List<Goal> getAllGoals() {
        return repository.findAll();
    }

    public Goal createGoal(Goal goal) {
        return repository.save(goal);
    }

    public Goal updateGoal(String id, Goal updated) {
        return repository.findById(id)
                .map(goal -> {
                    goal.setTitle(updated.getTitle());
                    goal.setDescription(updated.getDescription());
                    goal.setTargetDate(updated.getTargetDate());
                    goal.setStatus(updated.getStatus());
                    goal.setCategory(updated.getCategory());
                    return repository.save(goal);
                })
                .orElseThrow(() -> new RuntimeException("Goal not found: " + id));
    }

    public void deleteGoal(String id) {
        repository.deleteById(id);
    }

    public List<Goal> findByCategory(String category) {
        return repository.findByCategory(category);
    }

    public List<Goal> findByStatus(String status) {
        return repository.findByStatus(status);
    }

    public Goal markCompleted(String id) {
        return repository.findById(id)
                .map(goal -> {
                    goal.setStatus("completed");
                    Goal saved = repository.save(goal);

                    GoalCompletedEvent event = GoalCompletedEvent.newBuilder()
                            .setGoalId(saved.getGoalId())
                            .setCategory(saved.getCategory())
                            .setCompletedAt(Instant.now().toString())
                            .build();

                    producer.publish(event);

                    return saved;
                })
                .orElseThrow(() -> new RuntimeException("Goal not found: " + id));
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getResourcesForGoal(Goal goal) {
        String url = "http://localhost:8082/resources/category/" + goal.getCategory();
        return restTemplate.getForObject(url, List.class);
    }
}
