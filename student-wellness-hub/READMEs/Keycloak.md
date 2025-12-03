Student Wellness Hub — Assignment 2

A secure, production-ready microservices ecosystem built for COMP3095.
Includes API Gateway, Keycloak authentication, role-based access, and inter-service security.

Architecture Overview

This system consists of:

API Gateway (Spring Cloud Gateway) — single entry point, validates JWT tokens.

Keycloak — authentication, realm management, roles.

Goal Tracking Service — students can create wellness goals.

Event Service — students can register for events.

Wellness Resource Service — staff can create/manage wellness resources.

PostgreSQL / MongoDB / Redis — data layer for services.

Docker Compose — complete orchestration.

Authentication & Roles

The system uses Keycloak with the following roles:

Role	Permissions
student	create goals, register for events
staff	create/manage wellness resources
default-roles-wellnesshub	automatically assigned by Keycloak

These roles are assigned in Keycloak → Users → Role Mappings.

Gateway Security Rules

All incoming requests go through Gateway (http://localhost:8080).

Endpoint	Method	Who Can Access
/goals/**	POST/GET	student
/events/**	POST/GET	student
/resources/**	POST/GET	staff

JWT tokens must be included:

Authorization: Bearer <access_token>

How to Run the System
1. Start everything
   docker compose up -d


Wait until keycloak, api-gateway, and all services show “Running”.

Access Keycloak

URL: http://localhost:8090

Realm: wellnesshub

Default accounts:

Student: username student1, password password

Staff: username staff1, password password

(Can create more using admin login)

3. Issue Tokens via Postman

POST → http://localhost:8090/realms/wellnesshub/protocol/openid-connect/token

Body (x-www-form-urlencoded):

Key	Value
grant_type	password
client_id	wellness-gateway
username	student1 OR staff1
password	password

Copy the access token returned.

4. Use Secure Endpoints

Example: Create Goal (student)

POST http://localhost:8080/goals
Authorization: Bearer <student token>
Content-Type: application/json


Body:

{
"title": "Study 30 minutes",
"description": "COMP3095 review",
"targetDate": "2025-12-31",
"status": "in-progress",
"category": "academic"
}