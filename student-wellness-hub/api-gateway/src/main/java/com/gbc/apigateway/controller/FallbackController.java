package com.gbc.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/resources")
    public Mono<ResponseEntity<Map<String, Object>>> resourcesFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildFallbackResponse("Wellness Resource Service")));
    }

    @GetMapping("/goals")
    public Mono<ResponseEntity<Map<String, Object>>> goalsFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildFallbackResponse("Goal Tracking Service")));
    }

    @GetMapping("/events")
    public Mono<ResponseEntity<Map<String, Object>>> eventsFallback() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(buildFallbackResponse("Event Service")));
    }

    private Map<String, Object> buildFallbackResponse(String serviceName) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", serviceName + " is currently unavailable. Please try again later.");
        response.put("timestamp", LocalDateTime.now().toString());
        return response;
    }
}
