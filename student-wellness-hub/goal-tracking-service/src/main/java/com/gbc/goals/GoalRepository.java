package com.gbc.goals;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface GoalRepository extends MongoRepository<Goal, String> {
    List<Goal> findByCategory(String category);
    List<Goal> findByStatus(String status);
}
