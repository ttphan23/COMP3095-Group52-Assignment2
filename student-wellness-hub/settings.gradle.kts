pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/release") }
    }
}

rootProject.name = "student-wellness-hub"

include("wellness-resource-service")
include("goal-tracking-service")
include("event-service")
include("api-gateway")
