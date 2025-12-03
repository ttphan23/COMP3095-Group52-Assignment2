package com.gbc.wellness.controller;

import com.gbc.wellness.model.Resource;
import com.gbc.wellness.service.ResourceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Resource> getAll() {
        return service.getAllResources();
    }

    @GetMapping("/{id}")
    public Resource getById(@PathVariable Long id) {
        return service.getResource(id);
    }

    @GetMapping("/category/{category}")
    public List<Resource> getByCategory(@PathVariable String category) {
        return service.getByCategory(category);
    }

    @GetMapping("/search")
    public List<Resource> search(@RequestParam String keyword) {
        return service.searchByKeyword(keyword);
    }

    @PostMapping
    public Resource create(@RequestBody Resource resource) {
        return service.saveResource(resource);
    }

    @PutMapping("/{id}")
    public Resource update(@PathVariable Long id, @RequestBody Resource resource) {
        resource.setResourceId(id);
        return service.saveResource(resource);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteResource(id);
    }
}
