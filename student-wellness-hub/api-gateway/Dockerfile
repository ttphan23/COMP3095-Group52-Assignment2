# ---- build stage ----
FROM gradle:8.9-jdk21 AS build
WORKDIR /app
COPY . .
# produce the executable Spring Boot fat jar
RUN gradle clean bootJar -x test --no-daemon

# ---- run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]