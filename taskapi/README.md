# Task API — Spring Boot Learning Project

A Task Management REST API built with Spring Boot, designed as a hands-on learning project for Django developers transitioning to Java.

## Tech Stack

- **Java 17** + **Spring Boot 3.2**
- **Spring Web** — REST controllers
- **Spring Data JPA** — database access (Phase 2)
- **PostgreSQL** — database (Phase 2)
- **Bean Validation** — input validation (Phase 3)
- **Lombok** — reduce boilerplate
- **JUnit 5 + Mockito** — testing (Phase 4)

## Project Structure

```
src/main/java/com/example/taskapi/
├── TaskApiApplication.java      # Entry point (like manage.py)
├── controller/
│   ├── HealthController.java    # GET /api/health
│   └── TaskController.java      # CRUD endpoints for tasks
├── service/
│   └── TaskService.java         # Business logic
├── model/
│   └── Task.java                # Domain entity
├── dto/                         # Request/Response DTOs (Phase 3)
├── repository/                  # JPA repositories (Phase 2)
└── exception/                   # Custom exceptions (Phase 3)
```

## Quick Start

```bash
# 1. Make sure Java 17+ and Maven are installed
java -version
mvn -v

# 2. Run the app
cd taskapi
mvn spring-boot:run

# 3. Test the health endpoint
curl http://localhost:8080/api/health
# → {"status":"UP","timestamp":"...","message":"Task API is running! 🚀"}

# 4. Create a task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Learn Spring Boot", "description": "Build a real API"}'

# 5. List all tasks
curl http://localhost:8080/api/tasks
```

## API Endpoints

| Method | URL | Description | Status |
|---|---|---|---|
| GET | `/api/health` | Health check | ✅ Working |
| GET | `/api/tasks` | List all tasks | ✅ Working |
| GET | `/api/tasks/{id}` | Get task by ID | ✅ Working |
| POST | `/api/tasks` | Create a task | ✅ Working |
| PUT | `/api/tasks/{id}` | Update a task | ✅ Working |
| DELETE | `/api/tasks/{id}` | Delete a task | ✅ Working |

## Learning Path

Follow [build_first_action_plan.md](../build_first_action_plan.md) for the step-by-step guide.
