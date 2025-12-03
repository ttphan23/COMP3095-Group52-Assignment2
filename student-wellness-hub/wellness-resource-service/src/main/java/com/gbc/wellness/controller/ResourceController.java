package com.gbc.wellness.controller;

import com.gbc.wellness.model.Resource;
import com.gbc.wellness.service.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resources")
@Tag(name = "Resources", description = "Wellness Resource Management API")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @Operation(summary = "Get all resources", description = "Retrieves a list of all wellness resources")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all resources",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resource.class))))
    })
    @GetMapping
    public List<Resource> getAll() {
        return service.getAllResources();
    }

    @Operation(summary = "Get resource by ID", description = "Retrieves a specific wellness resource by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the resource",
                    content = @Content(schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content)
    })
    @GetMapping("/{id}")
    public Resource getById(
            @Parameter(description = "Resource ID", required = true)
            @PathVariable Long id) {
        return service.getResource(id);
    }

    @Operation(summary = "Get resources by category", description = "Retrieves all wellness resources in a specific category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved resources by category",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resource.class))))
    })
    @GetMapping("/category/{category}")
    public List<Resource> getByCategory(
            @Parameter(description = "Category name (e.g., fitness, mindfulness, nutrition)", required = true)
            @PathVariable String category) {
        return service.getByCategory(category);
    }

    @Operation(summary = "Search resources by keyword", description = "Searches wellness resources by keyword in title")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved matching resources",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resource.class))))
    })
    @GetMapping("/search")
    public List<Resource> search(
            @Parameter(description = "Search keyword", required = true)
            @RequestParam String keyword) {
        return service.searchByKeyword(keyword);
    }

    @Operation(summary = "Create a new resource", description = "Creates a new wellness resource (Staff only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource created successfully",
                    content = @Content(schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied - Staff role required", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Resource create(
            @Parameter(description = "Resource to create", required = true)
            @RequestBody Resource resource) {
        return service.saveResource(resource);
    }

    @Operation(summary = "Update a resource", description = "Updates an existing wellness resource (Staff only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource updated successfully",
                    content = @Content(schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied - Staff role required", content = @Content)
    })
    @PutMapping("/{id}")
    public Resource update(
            @Parameter(description = "Resource ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated resource data", required = true)
            @RequestBody Resource resource) {
        resource.setResourceId(id);
        return service.saveResource(resource);
    }

    @Operation(summary = "Delete a resource", description = "Deletes a wellness resource (Staff only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Resource not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied - Staff role required", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "Resource ID", required = true)
            @PathVariable Long id) {
        service.deleteResource(id);
    }
}
