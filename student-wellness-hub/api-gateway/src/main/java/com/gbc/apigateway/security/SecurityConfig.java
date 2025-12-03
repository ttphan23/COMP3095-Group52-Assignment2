package com.gbc.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex
                        // Actuator endpoints for health checks and Prometheus metrics
                        .pathMatchers("/actuator/**").permitAll()
                        
                        // Fallback endpoints for circuit breaker
                        .pathMatchers("/fallback/**").permitAll()
                        
                        // Swagger UI through API Gateway - requires staff role (ADMIN)
                        .pathMatchers("/api/*/swagger-ui/**", "/api/*/api-docs/**").hasRole("staff")

                        // Resource service - staff role for write operations
                        .pathMatchers(org.springframework.http.HttpMethod.POST, "/resources/**").hasRole("staff")
                        .pathMatchers(org.springframework.http.HttpMethod.PUT,  "/resources/**").hasRole("staff")
                        .pathMatchers(org.springframework.http.HttpMethod.DELETE,"/resources/**").hasRole("staff")

                        // Goal service - student role for goal creation
                        .pathMatchers(org.springframework.http.HttpMethod.POST, "/goals/**").hasRole("student")
                        
                        // Event service - student role for event registration
                        .pathMatchers(org.springframework.http.HttpMethod.POST, "/events/*/register").hasRole("student")

                        // All other requests require authentication
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
                )
                .build();
    }

    private ReactiveJwtAuthenticationConverterAdapter jwtAuthConverter() {
        JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();

        authConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?> roles) {
                for (Object role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));
                }
            }

            return authorities;
        });

        return new ReactiveJwtAuthenticationConverterAdapter(authConverter);
    }
}
