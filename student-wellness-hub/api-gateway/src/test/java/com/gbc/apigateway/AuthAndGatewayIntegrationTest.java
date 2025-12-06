package com.gbc.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.http.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthAndGatewayIntegrationTest {

    @Container
    static KeycloakContainer keycloak = new KeycloakContainer();

    @DynamicPropertySource
    static void config(DynamicPropertyRegistry reg) {
        reg.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloak.getAuthUrl() + "/realms/wellnesshub");
    }

    @Autowired
    TestRestTemplate rest;

    private String login() {
        String url = keycloak.getAuthUrl()
                + "/realms/wellnesshub/protocol/openid-connect/token";

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=password&client_id=wellness-client"
                + "&username=testuser&password=test123";

        ResponseEntity<Map> resp =
                rest.postForEntity(url, new HttpEntity<>(body, h), Map.class);

        return resp.getBody().get("access_token").toString();
    }

    @Test
    void shouldAccessProtectedResource() {
        String token = login();

        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(token);

        HttpEntity<Void> req = new HttpEntity<>(h);

        ResponseEntity<String> res =
                rest.exchange("/resources/protected", HttpMethod.GET, req, String.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturn401WithoutToken() {
        ResponseEntity<String> res =
                rest.getForEntity("/resources/protected", String.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldAuthenticateWithClientCredentials() {


        String url = keycloak.getAuthUrl()
                + "/realms/wellnesshub/protocol/openid-connect/token";

        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=client_credentials"
                + "&client_id=wellness-client"
                + "&client_secret=secret123";

        ResponseEntity<Map> resp =
                rest.postForEntity(url, new HttpEntity<>(body, h), Map.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody().get("access_token")).isNotNull();

        String token = resp.getBody().get("access_token").toString();


        HttpHeaders h2 = new HttpHeaders();
        h2.setBearerAuth(token);

        ResponseEntity<String> gatewayResp =
                rest.exchange("/resources/protected", HttpMethod.GET, new HttpEntity<>(h2), String.class);

        assertThat(gatewayResp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldReturnFallbackWhenServiceDown() {


        ResponseEntity<String> res =
                rest.getForEntity("/events/fetch", String.class);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).contains("fallback");
    }
}
