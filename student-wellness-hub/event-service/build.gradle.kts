import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask

plugins {
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
    java
}

group = "com.gbc"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven/")
}

configurations.all {
    resolutionStrategy {
        force("io.swagger.core.v3:swagger-annotations:2.2.22")
        force("io.swagger.core.v3:swagger-models:2.2.22")
    }
}

dependencies {
    // Core Spring + Database
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // OpenAPI / Swagger Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.22")

    // Kafka Consumer + Avro
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.confluent:kafka-avro-serializer:7.6.1") {
        exclude(group = "io.swagger.core.v3", module = "swagger-annotations")
    }
    implementation("org.apache.avro:avro:1.11.3")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:postgresql:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sourceSets {
    named("main") {
        java {
            srcDir("$buildDir/generated-main-avro-java")
        }
    }
}

tasks.withType<GenerateAvroJavaTask> {
    stringType.set("String")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
