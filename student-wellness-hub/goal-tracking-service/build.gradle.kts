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
    // --- Main dependencies ---
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.confluent:kafka-avro-serializer:7.6.1")
    implementation("org.apache.avro:avro:1.11.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // AssertJ
    testImplementation("org.assertj:assertj-core:3.25.3")


    val tcVersion = "1.20.3"

    testImplementation("org.testcontainers:testcontainers:$tcVersion")
    testImplementation("org.testcontainers:mongodb:$tcVersion")
    testImplementation("org.testcontainers:junit-jupiter:$tcVersion")
    testImplementation("org.testcontainers:kafka:$tcVersion")
    testImplementation("org.testcontainers:postgresql:$tcVersion")
    testImplementation("org.apache.commons:commons-compress:1.26.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")
    implementation("org.springframework.boot:spring-boot-starter-aop")
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

tasks.withType<Test> {
    useJUnitPlatform()
}
