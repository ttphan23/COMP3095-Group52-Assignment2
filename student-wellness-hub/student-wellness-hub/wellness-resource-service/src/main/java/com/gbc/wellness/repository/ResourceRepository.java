package com.gbc.wellness.repository;

import com.gbc.wellness.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByCategory(String category);
    List<Resource> findByTitleContainingIgnoreCase(String keyword);
}
