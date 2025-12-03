package com.gbc.goals.kafka;

import com.gbc.schemas.GoalCompletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class GoalCompletedProducer {

    private final KafkaTemplate<String, GoalCompletedEvent> kafkaTemplate;
    private static final String TOPIC = "goal-completed";

    public GoalCompletedProducer(KafkaTemplate<String, GoalCompletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(GoalCompletedEvent event) {
        kafkaTemplate.send(TOPIC, event.getGoalId().toString(), event);
    }
}
