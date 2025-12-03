package com.gbc.goals.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI goalTrackingOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Goal Tracking Service API")
                        .description("API for managing wellness goals in the Student Wellness Hub. " +
                                "This service provides endpoints for creating, reading, updating, and deleting " +
                                "wellness goals, tracking goal completion, and publishing goal completion events via Kafka.\n\n" +
                                "**Authentication:** This API requires a valid JWT Bearer token from Keycloak.\n" +
                                "**Roles:** Student role required for goal creation and completion.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Student Wellness Hub Team")
                                .email("wellness@gbc.ca"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("Local Development Server"),
                        new Server().url("http://localhost:8080").description("API Gateway"),
                        new Server().url("http://goal-tracking-service:8082").description("Docker Environment")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT Bearer token from Keycloak")));
    }
}
