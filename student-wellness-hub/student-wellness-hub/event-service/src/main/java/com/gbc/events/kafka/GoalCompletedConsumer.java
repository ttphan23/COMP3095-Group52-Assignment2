package com.gbc.events.kafka;

import com.gbc.events.Event;
import com.gbc.events.EventRepository;
import com.gbc.schemas.GoalCompletedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GoalCompletedConsumer {

    private final EventRepository repository;

    // cache: goalId -> recommended events
    private final Map<String, List<Event>> recommendations = new ConcurrentHashMap<>();

    public GoalCompletedConsumer(EventRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "goal-completed", groupId = "event-service")
    public void consume(GoalCompletedEvent event) {
        String goalId = event.getGoalId();      // Avro stringType = "String"
        String category = event.getCategory();  // Avro stringType = "String"

        List<Event> matches = repository.findByCategory(category);
        recommendations.put(goalId, matches);
    }

    public List<Event> getRecommendations(String goalId) {
        return recommendations.getOrDefault(goalId, Collections.emptyList());
    }
}
