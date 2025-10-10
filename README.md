# Student Wellness Hub – Microservices Project

### Team: Group 52
### Course: COMP3095 – Distributed Application Development

## Overview
This project implements a microservice-based wellness platform composed of:
1. **Wellness Resource Service (8081)** – manages mental health resources.
2. **Goal Tracking Service (8082)** – allows users to set and manage goals.
3. **Event Service (8083)** – manages wellness events and integrates with resources.

## Tech Stack
- Java 17, Spring Boot 3
- MongoDB, PostgreSQL, Redis
- Docker Compose
- Postman for testing

## How to Run
```bash
docker compose up --build
