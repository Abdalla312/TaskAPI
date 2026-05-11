# Task API

A production-ready Task Management REST API built with Spring Boot 3.2, featuring JWT authentication, role-based access control, database migrations with Flyway, and comprehensive OpenAPI/Swagger documentation.

## Overview

This project demonstrates a well-structured Spring Boot application with:
- **RESTful API** for managing tasks and users
- **JWT-based Authentication** (JJWT 0.12.7)
- **Role-based Authorization** 
- **Database Migrations** using Flyway
- **API Documentation** via SpringDoc OpenAPI/Swagger UI
- **Input Validation** with Bean Validation (Jakarta)
- **Object Mapping** with MapStruct
- **Pagination Support** for list endpoints

## Tech Stack

| Component      | Technology                                   |
|----------------|----------------------------------------------|
| **Language**   | Java 21                                      |
| **Framework**  | Spring Boot 3.2.4                            |
| **Database**   | PostgreSQL                                   |
| **ORM**        | Spring Data JPA (Hibernate)                  |
| **Security**   | Spring Security + JWT (JJWT 0.12.6 & 0.12.7) |
| **Migrations** | Flyway DB                                    |
| **Mapping**    | MapStruct 1.6.3                              |
| **API Docs**   | SpringDoc OpenAPI 2.5.0                      |
| **Validation** | Jakarta Bean Validation                      |
| **Build**      | Maven 3.11.0                                 |
| **Testing**    | JUnit 5 + Mockito                            |
| **Utilities**  | Lombok 1.18.32                               |

## Project Structure

```
src/main/java/com/example/taskapi/
├── TaskApiApplication.java      # Application entry point
├── auth/                         # Authentication endpoints
├── common/                       # Shared components
│   ├── HealthController.java    # Health check endpoint
│   ├── apiResponse/             # API response wrappers
│   ├── dto/                     # Data transfer objects
│   ├── exception/               # Exception handlers
│   ├── pagination/              # Pagination support
│   └── validation/              # Custom validation groups
├── config/                      # Spring configuration
├── task/                        # Task management module
│   ├── TaskController.java      # Task endpoints
│   ├── TaskService.java         # Business logic
│   ├── TaskRepository.java      # Database access
│   ├── TaskMapper.java          # DTO mapping
│   ├── Task.java                # Entity model
│   ├── TaskStatus.java          # Status enum
│   └── dto/                     # Task DTOs
└── user/                        # User management module
    ├── UserController.java      # User endpoints
    ├── UserService.java         # Business logic
    ├── UserRepository.java      # Database access
    ├── UserMapper.java          # DTO mapping
    ├── User.java                # Entity model
    ├── Role.java                # Role enum
    └── dto/                     # User DTOs
```

## API Endpoints

### Authentication (Public)
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and receive JWT token
- `POST /api/auth/refresh` - Refresh token request

### Health Check
- `GET /actuator/health` - Check API status

### Tasks (Protected)
- `GET /api/tasks` - List all tasks (paginated)
- `GET /api/tasks/{id}` - Get task by ID
- `GET /api/tasks?status=PENDING` - Filter tasks by status
- `GET /api/tasks?title=Example` - Search tasks by title
- `POST /api/tasks` - Create new task
- `PATCH /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task

### Users (Protected)
- `GET /api/users` - List all users (paginated)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PATCH /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

## Authentication & Security

### JWT Authentication

All endpoints except `/api/auth/*` are **protected** and require JWT authentication. After login/registration, include the JWT token in the `Authorization` header:

```bash
curl -H "Authorization: Bearer <your_jwt_token>" http://localhost:8000/api/tasks
```

### Roles

The application supports the following user roles:
- **ADMIN** - Administrative privileges
- **USER** - Standard user privileges (default)

### How JWT Works

1. User registers via `POST /api/auth/register` or logs in via `POST /api/auth/login`
2. API returns a JWT token in the response
3. Client includes token in subsequent requests in the `Authorization: Bearer <token>` header
4. Server validates the token before processing the request

## Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- PostgreSQL 12+
- Git

### Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd TaskAPI
   ```

2. **Verify prerequisites:**
   ```bash
   java -version          # Verify Java 17+
   mvn -v                 # Verify Maven
   ```

3. **Configure database and security:**
   - Create PostgreSQL database:
     ```sql
     CREATE DATABASE taskapi_db;
     ```
   - Set environment variables (add to `.env` or export):
     ```bash
     # Database configuration (optional - defaults to postgres/postgres)
     export DB_USERNAME=postgres
     export DB_PASSWORD=your_password
     
     # JWT secret (REQUIRED - generate a strong secret key)
     export JWT_SECRET=your-secret-key-here-minimum-32-characters-recommended
     ```

4. **Build the project:**
   ```bash
   mvn clean install
   ```

5. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```
   The API will start on `http://localhost:8000`

### Verify Installation

```bash
# Health check
curl http://localhost:8000/actuator/health

# Expected response:
{
  "status": "UP",
  "timestamp": "2026-04-28T12:00:00",
  "message": "Task API is running! 🚀"
}
```

## API Documentation

Access the interactive Swagger UI:
- **Swagger UI:** http://localhost:8000/swagger-ui/index.html
- **OpenAPI JSON:** http://localhost:8000/v3/api-docs

## Database Migrations

Migrations are managed with Flyway and located in `src/main/resources/db/Migration/`:
- `V1__create_users_table.sql` - Users table
- `V2__create_tasks_table.sql` - Tasks table
- `V3__fix_tasks_columns.sql` - Column adjustments
- `V4__align_tasks_schema_with_jpa.sql` - JPA alignment
- `V5__alter_userid_foreign_key_to_nullable.sql` - Foreign key adjustment
- `V6__add_role_to_users.sql` - Role support

Migrations run automatically on application startup.

## Configuration

Key settings in `src/main/resources/application.yml`:
- **Server port:** 8000
- **Database:** PostgreSQL (configurable via `DB_USERNAME` and `DB_PASSWORD` env vars)
- **JPA Hibernate DDL:** `validate` (schema validation only - depends on Flyway migrations)
- **Flyway:** Enabled with automatic migration on startup
- **JWT Secret:** Must be set via `JWT_SECRET` environment variable (no default value)

### Required Environment Variables

| Variable      | Description                      | Example               |
|---------------|----------------------------------|-----------------------|
| `JWT_SECRET`  | Secret key for JWT token signing | `my-super-secret-key` |
| `DB_USERNAME` | PostgreSQL username              | `postgres`            |
| `DB_PASSWORD` | PostgreSQL password              | `your_password`       |

## Data Models

### Task
- **id** (Long) - Primary key, auto-generated
- **title** (String) - Task title, required, max 50 characters
- **description** (String) - Task description, required, long text
- **status** (TaskStatus) - Task status, required - Valid values: `TODO`, `IN_PROGRESS`, `DONE`
- **user_id** (Long) - Foreign key to assigned user, nullable
- **created_at** (LocalDateTime) - Auto-set on creation, not updatable
- **updated_at** (LocalDateTime) - Auto-updated on any modification
- **Index:** On `status` column for efficient filtering

### User
- **id** (Long) - Primary key, auto-generated
- **username** (String) - Unique username, required, max 50 characters
- **email** (String) - Unique email, required
- **password** (String) - Bcrypt-hashed password, required, write-only in responses
- **role** (Role) - User role, required - Valid values: `ADMIN`, `USER` (default)
- **created_at** (LocalDateTime) - Auto-set on creation, not updatable
- **updated_at** (LocalDateTime) - Auto-updated on any modification
- **Relationship:** One-to-many with Tasks (cascade delete)

## Development

### Build & Test
```bash
# Build project
mvn clean package

# Run tests
mvn test

# Run with debug
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005"
```

### Testing Notes
- Auth endpoints are covered by MockMvc integration tests (see `src/test/java/com/example/taskapi/auth/AuthControllerIntegrationTest.java`).
- Tests run under the `test` Spring profile via `@ActiveProfiles("test")` in integration specs.

### Key Dependencies
- **Spring Boot Starters:** Web, Data JPA, Security, Validation
- **PostgreSQL Driver:** runtime scope
- **JJWT:** JWT token generation and validation
- **MapStruct:** Type-safe object mapping
- **SpringDoc OpenAPI:** API documentation generation
- **Lombok:** Boilerplate reduction

## Features

✅ **RESTful API Design** - Clean endpoint structure with proper HTTP methods and status codes
✅ **JWT Authentication** - Secure token-based authentication
✅ **Role-Based Access Control** - User roles support (ADMIN, USER, etc.)
✅ **Pagination** - Efficient handling of large datasets
✅ **Input Validation** - Comprehensive request validation
✅ **Error Handling** - Centralized exception handling with meaningful error responses
✅ **Database Migrations** - Version-controlled schema changes
✅ **API Documentation** - Auto-generated and interactive Swagger UI
✅ **Type-Safe Mapping** - MapStruct for DTOs
✅ **Security Best Practices** - Spring Security configuration

## Example Requests

### Register a New User
```bash
curl -X POST http://localhost:8000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "secure_password"
  }'

# Response:
{
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "message": "Registered successfully",
  "timestamp": "2026-04-28T12:00:00"
}
```

### Login
```bash
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "secure_password"
  }'

# Response:
{
  "data": {
    "id": 1,
    "username": "john_doe",
    "email": "john@example.com",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "message": "Login successful",
  "timestamp": "2026-04-28T12:00:00"
}
```

### Create a Task
```bash
# Note: Replace <token> with the JWT token received from login/register
curl -X POST http://localhost:8000/api/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "title": "Complete project documentation",
    "description": "Write comprehensive docs and examples",
    "status": "TODO"
  }'

# Valid status values: TODO, PENDING, IN_PROGRESS, COMPLETED
```

### Create a User
```bash
# Note: Replace <token> with the JWT token received from login/register
curl -X POST http://localhost:8000/api/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "username": "jane_smith",
    "email": "jane@example.com",
    "password": "another_secure_password",
    "role": "USER"
  }'
```

## Why This Project

- **Production-Ready:** Follows Spring Boot best practices and clean architecture principles
- **Well-Structured:** Clear separation of concerns with dedicated modules
- **Maintainable:** Type-safe mapping, dependency injection, and comprehensive configuration
- **Scalable:** Pagination, proper ORM usage, and database migrations
- **Documented:** OpenAPI/Swagger integration for API exploration
- **Secure:** JWT authentication and role-based access control

## License

This project is open source and available under the MIT License.

## Author

Built with ❤️ as a Spring Boot learning project demonstrating production-ready patterns and best practices.