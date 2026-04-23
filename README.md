# Task API

Task Management REST API built with Spring Boot.

## Tech Stack

- Java 17
- Spring Boot 3.2
- Spring Web
- Spring Data JPA
- Spring Security
- Flyway
- PostgreSQL
- Bean Validation
- JUnit 5 + Mockito

## Project Structure

src/main/java/com/example/taskapi/
- auth/
- common/
- config/
- task/
- user/
- TaskApiApplication.java

## Quick Start

1. Ensure Java 17+ and Maven are installed:
   - java -version
   - mvn -v
2. Configure database and environment values as needed.
3. Run the app from repository root:
   - mvn spring-boot:run
4. Health check:
   - GET http://localhost:8000/api/health

## API Docs

- Swagger UI: http://localhost:8000/swagger-ui/index.html

## Why This Repo Is Public-Ready

- Single focused project (Task API only)
- Clean structure and commit history
- No unrelated practice folders or generated build artifacts tracked
