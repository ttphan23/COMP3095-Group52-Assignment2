package com.gbc.wellness;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.http.*;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class WellnessResourceServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("wellnessdb")
                    .withUsername("postgres")
                    .withPassword("password");

    @DynamicPropertySource
    static void dbProps(DynamicPropertyRegistry reg) {
        reg.add("spring.datasource.url", postgres::getJdbcUrl);
        reg.add("spring.datasource.username", postgres::getUsername);
        reg.add("spring.datasource.password", postgres::getPassword);
        reg.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    TestRestTemplate rest;

    @Test
    void shouldCreateAndGetResource() {

        Map<String, Object> req = Map.of(
                "title", "Integration Test",
                "description", "Testing Postgres CRUD"
        );

        ResponseEntity<Map> createRes =
                rest.postForEntity("/resources", req, Map.class);

        assertThat(createRes.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Long id = Long.valueOf(createRes.getBody().get("resourceId").toString());

        ResponseEntity<Map> getRes =
                rest.getForEntity("/resources/" + id, Map.class);

        assertThat(getRes.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getRes.getBody().get("title")).isEqualTo("Integration Test");
    }
}
