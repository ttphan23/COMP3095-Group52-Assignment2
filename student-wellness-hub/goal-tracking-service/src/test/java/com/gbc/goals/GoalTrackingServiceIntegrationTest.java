package com.gbc.goals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.util.Map;

import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GoalTrackingServiceIntegrationTest {

    @Container
    static MongoDBContainer mongo =
            new MongoDBContainer(DockerImageName.parse("mongo:6.0"));

    @Container
    static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry reg) {
        reg.add("spring.data.mongodb.uri", mongo::getConnectionString);
        reg.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    TestRestTemplate rest;

    @Test
    void shouldCreateGoal() {
        Map<String, Object> req = Map.of(
                "title", "Run 5km",
                "description", "Daily running goal"
        );

        ResponseEntity<Map> res =
                rest.postForEntity("/goals", req, Map.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(res.getBody().get("title")).isEqualTo("Run 5km");
    }
}
