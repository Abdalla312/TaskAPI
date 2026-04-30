# AGENTS.md

## Scope
- This guide applies to the whole repository (`/`).
- Primary stack: Spring Boot 3.2 + Java 17 + PostgreSQL + Flyway (`pom.xml`, `src/main/resources/application.yml`).

## Architecture at a glance
- Feature modules are grouped by package: `auth`, `task`, `user`, with shared concerns under `common` and `security`.
- Standard request flow is `Controller -> Service -> Repository -> Entity`, with DTO mapping via MapStruct (`TaskMapper`, `UserMapper`).
- API responses are wrapped in `ApiResponse<T>` and paginated lists in `PageResponse<T>` (`common/apiResponse/ApiResponse.java`, `common/pagination/PageResponse.java`).
- Global error handling is centralized in `GlobalExceptionHandler`; services throw typed exceptions (`ResourceNotFoundException`, `BadRequestException`, etc.).

## Security and auth flow
- Stateless JWT auth is enforced by `SecurityConfig` + `JwtAuthFilter`; all routes require auth except `/api/auth/**` and `/actuator/health`.
- DELETE endpoints for `/api/tasks/**` and `/api/users/**` require `ROLE_ADMIN` (configured in `SecurityConfig`).
- Login accepts username OR email (`AuthRequest.identifier` -> `CustomUserDetailsService.findByUsernameOrEmail`).
- Access token creation is in `JwtService`; token contains `userId` and `role` claims.
- Refresh token persistence exists (`RefreshToken` entity + `refresh_tokens` migration), but current auth code has mismatched method/property usage; check `AuthService` and `RefreshTokenService` before extending auth behavior.

## Data and schema rules
- JPA uses `ddl-auto: validate`; schema changes must be done through Flyway migrations in `src/main/resources/db/Migration`.
- `tasks.user_id` is intentionally nullable (unassigned tasks are supported in `TaskService.createTask`).
- Task status values in code/migrations are `TODO | IN_PROGRESS | DONE` (`TaskStatus`, `V4__align_tasks_schema_with_jpa.sql`).
- Keep entity/DTO/DB enums aligned when changing status values (validation and check constraints must match).

## Project-specific coding patterns
- For PATCH updates, preserve entity identity and ignore null DTO fields via MapStruct `@BeanMapping(nullValuePropertyMappingStrategy = IGNORE)` (`TaskMapper.updateEntityFromDto`, `UserMapper.updateEntityFromDto`).
- `TaskRequest` validation uses groups (`OnCreate`, `OnPatch`); do not replace with plain `@Valid` on task create/patch endpoints.
- New controllers should keep existing response style: `ApiResponse.ok(...)` / `ApiResponse.created(...)` and `ResponseEntity` status codes.
- Services are the place for business rules (uniqueness checks, assignment logic, role defaults), not controllers.

## Developer workflows
- Build: `mvn clean package`
- Tests: `mvn test` (currently only context-load test in `src/test/java/com/example/taskapi/TaskApiApplicationTests.java`).
- Run locally: `mvn spring-boot:run` (server port `8000`).
- Required env/config for startup: PostgreSQL at `taskapi_db` and `JWT_SECRET` (base64-compatible for `JwtService`).
- API docs/UI: `/swagger-ui/index.html`; health: `/actuator/health`.

## High-impact files to read first
- `src/main/java/com/example/taskapi/security/SecurityConfig.java`
- `src/main/java/com/example/taskapi/security/JwtAuthFilter.java`
- `src/main/java/com/example/taskapi/task/TaskService.java`
- `src/main/java/com/example/taskapi/user/UserService.java`
- `src/main/java/com/example/taskapi/common/exception/GlobalExceptionHandler.java`
- `src/main/resources/db/Migration/*.sql`

