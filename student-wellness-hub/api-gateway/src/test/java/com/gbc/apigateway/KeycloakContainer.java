package com.gbc.apigateway;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.MountableFile;

public class KeycloakContainer extends GenericContainer<KeycloakContainer> {

    public KeycloakContainer() {
        super("quay.io/keycloak/keycloak:25.0.6");

        withExposedPorts(8080);
        withCommand("start-dev --import-realm");

        withCopyFileToContainer(
                MountableFile.forClasspathResource("realm-export.json"),
                "/opt/keycloak/data/import/realm-export.json"
        );
    }

    public String getAuthUrl() {
        return "http://" + getHost() + ":" + getMappedPort(8080);
    }
}
