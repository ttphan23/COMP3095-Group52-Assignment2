package com.gbc.events;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventServiceIntegrationTest {


    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("wellnessdb")
                    .withUsername("postgres")
                    .withPassword("password");


    @Container
    static KafkaContainer kafka =
            new KafkaContainer(
                    DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
            );

    @DynamicPropertySource

    static void props(DynamicPropertyRegistry reg) {
        reg.add("spring.datasource.url", postgres::getJdbcUrl);
        reg.add("spring.datasource.username", postgres::getUsername);
        reg.add("spring.datasource.password", postgres::getPassword);

        reg.add("spring.jpa.hibernate.ddl-auto", () -> "create");   // ⭐ MUST ADD
        reg.add("spring.jpa.show-sql", () -> "true");

        reg.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }


    @Autowired
    TestRestTemplate rest;

    @Test
    void shouldPublishEvent() {

        Map<String, Object> req = Map.of(
                "title", "Integration Test Event",
                "description", "Test description",
                "location", "Online",
                "date", "2025-12-05",
                "capacity", 100,
                "category", "Wellness"
        );

        ResponseEntity<String> res =
                rest.postForEntity("/events", req, String.class);

        // 고정값 검사
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // 또는 유연하게
        // assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
    }

}
