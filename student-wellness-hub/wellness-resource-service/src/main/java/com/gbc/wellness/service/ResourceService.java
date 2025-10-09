package com.gbc.wellness.service;

import com.gbc.wellness.model.Resource;
import com.gbc.wellness.repository.ResourceRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository repository;

    public ResourceService(ResourceRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "resources", key = "#id")
    public Resource getResource(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Cacheable(value = "resources")
    public List<Resource> getAllResources() {
        return repository.findAll();
    }

    @Cacheable(value = "resourcesByCategory", key = "#category")
    public List<Resource> getByCategory(String category) {
        return repository.findByCategory(category);
    }

    @Cacheable(value = "resourcesByKeyword", key = "#keyword")
    public List<Resource> searchByKeyword(String keyword) {
        return repository.findByTitleContainingIgnoreCase(keyword);
    }

    @CachePut(value = "resources", key = "#result.resourceId")
    public Resource saveResource(Resource resource) {
        return repository.save(resource);
    }

    @CacheEvict(value = "resources", key = "#id")
    public void deleteResource(Long id) {
        repository.deleteById(id);
    }
}
