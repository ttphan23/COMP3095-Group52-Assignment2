package com.gbc.wellness.service;

import com.gbc.wellness.model.Resource;
import com.gbc.wellness.repository.ResourceRepository;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository repository;

    public ResourceService(ResourceRepository repository) {
        this.repository = repository;
    }

    // READS
    @Cacheable(value = "resourceById", key = "#id")
    public Resource getResource(Long id) {
        return repository.findById(id).orElseThrow();
    }

    @Cacheable(value = "resourcesAll")
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

    // WRITES
    @Caching(
            put = {@CachePut(value = "resourceById", key = "#result.resourceId")},
            evict = {
                    @CacheEvict(value = "resourcesAll", allEntries = true),
                    @CacheEvict(value = "resourcesByCategory", allEntries = true),
                    @CacheEvict(value = "resourcesByKeyword", allEntries = true)
            }
    )
    public Resource saveResource(Resource resource) {
        return repository.save(resource);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "resourceById", key = "#id"),
                    @CacheEvict(value = "resourcesAll", allEntries = true),
                    @CacheEvict(value = "resourcesByCategory", allEntries = true),
                    @CacheEvict(value = "resourcesByKeyword", allEntries = true)
            }
    )
    public void deleteResource(Long id) {
        repository.deleteById(id);
    }
}
