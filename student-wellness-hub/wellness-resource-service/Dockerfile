# ---- build stage ----
FROM gradle:8.9-jdk21 AS build
WORKDIR /app
COPY . .
# produce the fat jar
RUN gradle clean bootJar -x test

# ---- run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
# EXPOSE is optional; set to each service port
# EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]
