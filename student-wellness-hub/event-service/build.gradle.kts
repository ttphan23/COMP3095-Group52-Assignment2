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

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop") // Resilience4j AOP


    runtimeOnly("org.postgresql:postgresql")


    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.confluent:kafka-avro-serializer:7.6.3")
    implementation("org.apache.avro:avro:1.11.4")


    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")


    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("io.micrometer:micrometer-registry-prometheus")


    testImplementation("org.springframework.boot:spring-boot-starter-test") {

        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(group = "junit", module = "junit")
    }

    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")


    val testcontainersVersion = "1.19.8"
    testImplementation("org.testcontainers:junit-jupiter:$testcontainersVersion")
    testImplementation("org.testcontainers:postgresql:$testcontainersVersion")
    testImplementation("org.testcontainers:kafka:$testcontainersVersion")
    testImplementation("org.testcontainers:mongodb:$testcontainersVersion")
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
