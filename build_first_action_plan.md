# Build-First Action Plan: Java Spring Boot for Django Developers

> **Philosophy**: Stop studying Java in isolation. Build a Spring Boot app and learn Java as you go.  
> **Timeline**: ~6 weeks at 2–3 hours/day. Adjust as needed.  
> **Starting date**: March 28, 2026

---

## What You Already Know (Django → Spring Translation)

| Django Concept | Spring Boot Equivalent | Notes |
|---|---|---|
| `models.py` | `@Entity` classes | JPA annotations instead of field classes |
| `views.py` / `@api_view` | `@RestController` + `@GetMapping` etc. | Methods return objects, not `Response()` |
| `serializers.py` | DTOs (plain Java classes) | No magic — just POJOs with getters/setters |
| `urls.py` | `@RequestMapping` on controllers | Routes live on the controller, not in a separate file |
| `settings.py` | `application.yml` | One config file, profiles for dev/prod |
| Django ORM QuerySet | `JpaRepository` methods | `findById()`, `findByStatus()`, etc. |
| `manage.py migrate` | Flyway migration SQL files | Write raw SQL migrations, not auto-generated |
| Django middleware | `Filter` / `HandlerInterceptor` | Similar concept, different mechanism |
| Django signals | `@EventListener` | Same pub/sub pattern |
| `pip install` + `requirements.txt` | Maven `pom.xml` dependencies | XML is ugly but you only touch it occasionally |
| Django Admin | — (no equivalent) | Build your own admin endpoints or use SpringDoc UI |
| `python manage.py runserver` | `mvn spring-boot:run` | Runs on port 8080 by default |

---

## Phase 1 — Hello World to CRUD (Days 1–3)

**Goal**: A running Spring Boot app with an in-memory Task CRUD API.

### Day 1: First Boot (2 hrs)
- [ ] Open the `taskapi/` project I created for you
- [ ] Run `mvn spring-boot:run` — verify it starts on port 8080
- [ ] Hit `http://localhost:8080/api/health` in browser — see `{"status": "UP"}`
- [ ] Read through the starter code — understand `@RestController`, `@GetMapping`
- [ ] **Java you'll learn**: annotations, method return types, String

**Deliverable**: App runs, health endpoint works ✅

### Day 2: Task Controller — List & Create (3 hrs)
- [ ] Create `TaskController` with `@RestController` and `@RequestMapping("/api/tasks")`
- [ ] Store tasks in a `List<Task>` in-memory for now (no database yet)
- [ ] `POST /api/tasks` — accepts JSON body, adds to list, returns `201 Created`
- [ ] `GET /api/tasks` — returns all tasks as JSON
- [ ] Test with curl or Postman
- [ ] **Java you'll learn**: `ArrayList`, generics (`List<Task>`), `@RequestBody`, JSON serialization

**Deliverable**: Can create and list tasks via API ✅

### Day 3: Full CRUD + Service Layer (3 hrs)
- [ ] Extract business logic into `TaskService` (controller should only handle HTTP)
- [ ] Add `GET /api/tasks/{id}`, `PUT /api/tasks/{id}`, `DELETE /api/tasks/{id}`
- [ ] Use `@PathVariable` for ID extraction
- [ ] Return proper status codes: `200`, `201`, `204`, `404`
- [ ] Add a simple `TaskNotFoundException` that returns 404
- [ ] **Java you'll learn**: `HashMap`, `Optional`, exceptions, `ResponseEntity`

**Deliverable**: Full CRUD working, tested via Postman/curl ✅

---

## Phase 2 — Database Integration (Days 4–7)

**Goal**: Replace in-memory storage with PostgreSQL using JPA.

### Day 4: Entity + Repository (3 hrs)
- [ ] Add `@Entity` annotations to `Task` model
- [ ] Create `TaskRepository extends JpaRepository<Task, Long>`
- [ ] Configure PostgreSQL connection in `application.yml`
- [ ] Switch `TaskService` to use `TaskRepository` instead of `HashMap`
- [ ] Run app — verify data persists in PostgreSQL
- [ ] **Java you'll learn**: JPA annotations, interfaces, generics in practice

**Deliverable**: Tasks persist in PostgreSQL ✅

### Day 5: Relationships + Enums (3 hrs)
- [ ] Add `User` entity with `id`, `name`, `email`
- [ ] Add `TaskStatus` enum: `TODO`, `IN_PROGRESS`, `DONE`
- [ ] Create `@ManyToOne` relationship: Task belongs to a User
- [ ] Create `UserRepository` and `UserService`
- [ ] `POST /api/users` and `GET /api/users`
- [ ] Assign tasks to users: `PUT /api/tasks/{id}/assign/{userId}`
- [ ] **Java you'll learn**: enums (as classes), `@Enumerated`, relationship mapping

**Deliverable**: Users + Tasks with relationships in DB ✅

### Day 6: Flyway Migrations (2 hrs)
- [ ] Add Flyway dependency to `pom.xml`
- [ ] Delete the auto-generated tables from `spring.jpa.hibernate.ddl-auto=create`
- [ ] Write `V1__create_users_table.sql` and `V2__create_tasks_table.sql`
- [ ] Switch to `ddl-auto=validate` — Flyway handles schema now
- [ ] Run app — confirm tables created by Flyway
- [ ] **Django parallel**: This is exactly like `makemigrations` + `migrate`, but you write SQL manually

**Deliverable**: Database managed by migration scripts ✅

### Day 7: Custom Queries + Streams (2 hrs)
- [ ] Add query methods: `findByStatus()`, `findByTitleContainingIgnoreCase()`
- [ ] Write a `@Query` with JPQL for complex queries
- [ ] Use Java Streams in service layer: filter, map, collect
- [ ] `GET /api/tasks?status=IN_PROGRESS` — filtered list
- [ ] **Java you'll learn**: Streams (`filter`, `map`, `collect`), lambda expressions

**Deliverable**: Filtered queries and stream-based transformations working ✅

---

## Phase 3 — Production Patterns (Days 8–11)

**Goal**: Add validation, error handling, DTOs, pagination — the patterns real APIs need.

### Day 8: DTOs + Mapping (2 hrs)
- [ ] Create `TaskRequest` DTO (what client sends) and `TaskResponse` DTO (what API returns)
- [ ] Never expose `@Entity` directly in API responses (same reason as DRF serializers)
- [ ] Create a `TaskMapper` class to convert between Entity ↔ DTO
- [ ] Update controller to accept `TaskRequest` and return `TaskResponse`
- [ ] **Django parallel**: DTOs = serializers, but without auto-magic. More verbose, more control.

**Deliverable**: API uses DTOs, entities never exposed ✅

### Day 9: Validation + Error Handling (3 hrs)
- [ ] Add Bean Validation annotations: `@NotBlank`, `@Size`, `@Email` on DTOs
- [ ] Use `@Valid` in controller parameters
- [ ] Create `GlobalExceptionHandler` with `@ControllerAdvice`
- [ ] Handle `MethodArgumentNotValidException` → return `400` with field errors
- [ ] Handle `ResourceNotFoundException` → return `404`
- [ ] Handle generic `Exception` → return `500` (never leak stack traces)
- [ ] Consistent error response format: `{ timestamp, status, error, message }`
- [ ] **Django parallel**: `@ControllerAdvice` ≈ DRF's `exception_handler`. Validation annotations ≈ serializer field validation.

**Deliverable**: Clean validation errors, consistent error JSON format ✅

### Day 10: Pagination + Sorting (2 hrs)
- [ ] Accept `Pageable` in repository and controller
- [ ] `GET /api/tasks?page=0&size=10&sort=createdAt,desc`
- [ ] Return `Page<TaskResponse>` with metadata (totalElements, totalPages)
- [ ] **Django parallel**: This is like DRF's `PageNumberPagination`, but built into Spring Data

**Deliverable**: Paginated task list with metadata ✅

### Day 11: Logging + API Docs (2 hrs)
- [ ] Add SLF4J logging in service layer: `log.info()`, `log.error()`
- [ ] Add SpringDoc/OpenAPI dependency
- [ ] Access Swagger UI at `http://localhost:8080/swagger-ui.html`
- [ ] Add `@Operation` and `@ApiResponse` annotations for documentation
- [ ] **Django parallel**: SpringDoc ≈ DRF's browsable API / drf-spectacular

**Deliverable**: API documented with Swagger UI ✅

---

## Phase 4 — Security + Testing + Polish (Days 12–16)

**Goal**: Add JWT auth, write tests, make it interview-ready.

### Day 12: Spring Security Basics (3 hrs)
- [ ] Add Spring Security + JWT dependencies
- [ ] Create `AuthController` with `POST /api/auth/register` and `POST /api/auth/login`
- [ ] Hash passwords with `BCryptPasswordEncoder`
- [ ] Generate JWT on successful login
- [ ] **Django parallel**: Like `django-rest-framework-simplejwt`, but you wire it yourself

**Deliverable**: Register and login endpoints return JWT ✅

### Day 13: Protected Routes + Roles (3 hrs)
- [ ] Create `JwtAuthenticationFilter` — extract and validate token from `Authorization` header
- [ ] Configure `SecurityFilterChain`: public routes vs protected routes
- [ ] Test: no token → `401`, valid token → `200`
- [ ] Add role-based access: `@PreAuthorize("hasRole('ADMIN')")`
- [ ] Test: USER role on admin endpoint → `403`

**Deliverable**: JWT auth protecting all task endpoints ✅

### Day 14: Unit Tests (3 hrs)
- [ ] Write unit tests for `TaskService` using JUnit 5 + Mockito
- [ ] `should_createTask_when_validInput()`
- [ ] `should_throwException_when_taskNotFound()`
- [ ] `should_returnFilteredTasks_when_statusProvided()`
- [ ] Use `@Mock`, `@InjectMocks`, `when().thenReturn()`, `verify()`
- [ ] **Django parallel**: `Mock` ≈ `unittest.mock.patch`, `@InjectMocks` ≈ auto-wiring mocks into the class under test

**Deliverable**: Service layer unit tests passing green ✅

### Day 15: Integration Tests (3 hrs)
- [ ] Write web layer tests with `@WebMvcTest` + `MockMvc`
- [ ] Write full integration test with `@SpringBootTest`
- [ ] Optional: add Testcontainers for real PostgreSQL in tests
- [ ] Run `mvn test` — all green

**Deliverable**: Unit + integration tests all passing ✅

### Day 16: Polish + README (2 hrs)
- [ ] Clean up code — consistent formatting, no TODOs
- [ ] Write `README.md`: description, tech stack, setup instructions, API endpoints
- [ ] Verify fresh `git clone` → `mvn spring-boot:run` works
- [ ] Practice 2-minute project walkthrough

**Deliverable**: Interview-ready project ✅

---

## Java Concepts — Learn As You Go (Reference)

Don't study these in advance. Come back here when you hit them during building.

| When You Hit It | Java Concept | Quick Explanation |
|---|---|---|
| Day 1 | Annotations (`@`) | Metadata on classes/methods. Spring reads them at startup. Like Python decorators but for metadata. |
| Day 2 | Generics (`List<Task>`) | Type-safe collections. `List<Task>` = "a list that only holds Task objects". |
| Day 2 | `ArrayList` | Like Python's `list`. Dynamic array. |
| Day 3 | `HashMap` | Like Python's `dict`. Key-value store. |
| Day 3 | `Optional<T>` | A container that may or may not hold a value. Replaces `null` checks. Like `value or None` but explicit. |
| Day 3 | Exceptions | `throw new XException()` = `raise XException()`. Java has checked (must catch) and unchecked (don't have to). |
| Day 5 | Enums | Like Python `Enum` but more powerful — can have methods and fields. |
| Day 7 | Streams | `list.stream().filter().map().collect()` ≈ list comprehensions + `filter()` in Python. |
| Day 7 | Lambdas | `x -> x.getName()` ≈ `lambda x: x.name` in Python. |
| Day 8 | POJOs/DTOs | Plain Old Java Objects. Just a class with fields + getters/setters. No framework magic. |
| Day 12 | Interfaces | Like Python's `ABC`. Define a contract. Classes `implement` them. |

---

## Rules to Keep You Moving

1. **Never spend more than 30 min stuck on a Java syntax issue.** Google it, copy the pattern, move on.
2. **One deliverable per day.** If you can hit the API and see results, the day is done.
3. **Skip what's not blocking you.** If generics wildcards confuse you, just use `List<Task>` and move on.
4. **Compare to Django constantly.** "How would I do this in Django?" Then find the Spring equivalent.
5. **Commit after every working feature.** Git history = progress proof = motivation.
6. **If you finish early, add a feature.** Don't study ahead — build ahead.
