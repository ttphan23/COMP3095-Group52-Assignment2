# Student Wellness Hub - Microservices

This project consists of three microservices for a student wellness hub application:

- **wellness-resource-service** (Port 8082) - Manages wellness resources using PostgreSQL and Redis
- **goal-tracking-service** (Port 8083) - Tracks student goals using MongoDB
- **event-service** (Port 8084) - Manages wellness events using PostgreSQL

## Prerequisites

- Java 17 (JDK 17 or higher)
- Docker and Docker Compose **MUST be running** for:
  - Integration tests (Testcontainers)
  - Docker Compose deployment
- Gradle (or use the included Gradle wrapper)

### Important Notes
- **Before running tests**: Make sure Docker Desktop (or Docker daemon) is running
- **IDE Linter Errors**: If you see "cannot be resolved" errors for Testcontainers in your IDE:
  1. Run `./gradlew --stop` to stop Gradle daemon
  2. Run `./gradlew clean build` to download dependencies
  3. Refresh/reimport Gradle project in your IDE (IntelliJ: Gradle → Reload All; VSCode: Reload Window)
- The build files use Java 17 compatibility settings

## Architecture

### Databases
- **PostgreSQL** - Used by wellness-resource-service and event-service
- **MongoDB** - Used by goal-tracking-service
- **Redis** - Used by wellness-resource-service for caching

## Verifying Your Setup

### 1. Verify Java Installation
```bash
java -version
# Should show Java 17 or higher
```

### 2. Verify Docker is Running
```bash
docker --version
docker ps
# Should show running containers or at least not give connection errors
```

### 3. Build All Services
```bash
cd student-wellness-hub
./gradlew clean build -x test
# Should show BUILD SUCCESSFUL for all services
```

## Running Integration Tests with Testcontainers

Each service includes integration tests that use Testcontainers to spin up actual database instances for testing.

### Run tests for a specific service:

```bash
# Event Service (PostgreSQL)
cd event-service
./gradlew test

# Goal Tracking Service (MongoDB)
cd goal-tracking-service
./gradlew test

# Wellness Resource Service (PostgreSQL)
cd wellness-resource-service
./gradlew test
```

### Run all tests from the root:

```bash
cd student-wellness-hub
./gradlew test
```

**Important:** 
- ⚠️ **Docker MUST be running** before executing tests
- Testcontainers will automatically:
  - Start the required database containers
  - Run the tests
  - Clean up the containers after completion
- First test run may take several minutes as Docker images are downloaded

## Running with Docker Compose

### Start all services:

```bash
cd student-wellness-hub
docker-compose up --build
```

This will:
1. Start PostgreSQL, MongoDB, and Redis containers
2. Build and start all three microservices
3. Configure proper networking between services

### Start services in detached mode:

```bash
docker-compose up -d --build
```

### View logs:

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f wellness-resource-service
docker-compose logs -f goal-tracking-service
docker-compose logs -f event-service
```

### Stop all services:

```bash
docker-compose down
```

### Stop and remove volumes (clean slate):

```bash
docker-compose down -v
```

## Service URLs

When running with Docker Compose:

- **Wellness Resource Service**: http://localhost:8082
- **Goal Tracking Service**: http://localhost:8083
- **Event Service**: http://localhost:8084

## Database Access

When running with Docker Compose:

### PostgreSQL
- Host: localhost
- Port: 5432
- Database: wellnessdb
- Username: postgres
- Password: password

### MongoDB
- Host: localhost
- Port: 27017
- Database: student_wellness

### Redis
- Host: localhost
- Port: 6379

## API Endpoints

### Wellness Resource Service (8082)
- `GET /api/resources` - Get all resources
- `GET /api/resources/{id}` - Get resource by ID
- `POST /api/resources` - Create new resource
- `PUT /api/resources/{id}` - Update resource
- `DELETE /api/resources/{id}` - Delete resource
- `GET /api/resources/category/{category}` - Get resources by category
- `GET /api/resources/search?keyword={keyword}` - Search resources

### Goal Tracking Service (8083)
- `GET /api/goals` - Get all goals
- `GET /api/goals/{id}` - Get goal by ID
- `POST /api/goals` - Create new goal
- `PUT /api/goals/{id}` - Update goal
- `DELETE /api/goals/{id}` - Delete goal
- `GET /api/goals/category/{category}` - Get goals by category
- `GET /api/goals/status/{status}` - Get goals by status

### Event Service (8084)
- `GET /api/events` - Get all events
- `GET /api/events/{id}` - Get event by ID
- `POST /api/events` - Create new event
- `PUT /api/events/{id}` - Update event
- `DELETE /api/events/{id}` - Delete event
- `GET /api/events/date/{date}` - Get events by date
- `GET /api/events/location/{location}` - Get events by location
- `POST /api/events/{id}/register/{studentId}` - Register student for event

## Development

### Run a service locally (without Docker):

First, start the required databases using Docker Compose:

```bash
docker-compose up postgres mongodb redis
```

Then run the service:

```bash
cd <service-name>
./gradlew bootRun
```

### Build JARs:

```bash
# Build all services
./gradlew build

# Build specific service
cd <service-name>
./gradlew build
```

## Testing

### Integration Tests
Each service has comprehensive integration tests using Testcontainers:

- **EventServiceIntegrationTest** - Tests CRUD operations with PostgreSQL
- **GoalTrackingServiceIntegrationTest** - Tests CRUD operations with MongoDB
- **WellnessResourceServiceIntegrationTest** - Tests CRUD operations with PostgreSQL

The tests verify:
- Creating entities
- Reading entities (findById, findAll, custom queries)
- Updating entities
- Deleting entities
- Repository custom query methods

## Project Structure

```
student-wellness-hub/
├── event-service/
│   ├── src/
│   │   ├── main/java/com/gbc/events/
│   │   └── test/java/com/gbc/events/
│   ├── Dockerfile
│   └── build.gradle.kts
├── goal-tracking-service/
│   ├── src/
│   │   ├── main/java/com/gbc/goals/
│   │   └── test/java/com/gbc/goals/
│   ├── Dockerfile
│   └── build.gradle.kts
├── wellness-resource-service/
│   ├── src/
│   │   ├── main/java/com/gbc/wellness/
│   │   └── test/java/com/gbc/wellness/
│   ├── Dockerfile
│   └── build.gradle.kts
├── docker-compose.yml
└── README.md
```

## Troubleshooting

### Port conflicts
If you get port binding errors, make sure the ports (5432, 27017, 6379, 8082, 8083, 8084) are not already in use:

```bash
# Windows
netstat -ano | findstr "8082"
netstat -ano | findstr "5432"

# Linux/Mac
lsof -i :8082
lsof -i :5432
```

### Container issues
If services can't connect to databases, ensure health checks are passing:

```bash
docker-compose ps
```

### Clean restart
```bash
docker-compose down -v
docker system prune -a
docker-compose up --build
```

## Notes

- All services use environment variables for configuration, allowing easy deployment to different environments
- Health checks ensure databases are ready before services start
- The wellness-resource-service is started first as other services may depend on it
- Each service can be scaled independently
- All databases persist data using Docker volumes

