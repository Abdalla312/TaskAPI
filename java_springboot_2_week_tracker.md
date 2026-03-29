# Java + Spring Boot 2-Week Interview Prep Tracker

Period: March 10, 2026 to March 23, 2026 *(original schedule — adjust dates as needed for your actual pace)*

## Goal
Get interview-ready for a backend training role focused on Java and Spring Boot by:
- mastering core Java fundamentals
- building one complete Spring Boot backend project
- practicing common interview questions and project explanation

## Recommended Daily Cadence
- 2 hours learning
- 2 hours coding
- 20 minutes interview drill (speak answers out loud)

## Environment Setup Checklist
- [x] `java -version` works
- [x] `javac -version` works
- [x] `mvn -v` works (install Maven if missing — you'll need it for Spring Boot)
- [ ] Understand Maven basics: `pom.xml`, dependencies, `mvn clean install`, `mvn spring-boot:run`
- [x] VS Code Java extensions installed
- [x] PostgreSQL installed and reachable
- [x] Git repo initialized for your practice project
- [x] Postman or Insomnia installed (for API testing in Week 2)
- [x] Know basic Git workflow: `add`, `commit`, `push`, `branch`, `merge`

## Progress Dashboard

Use `Not Started`, `In Progress`, or `Done` in the Status column.

| Day | Date | Focus | Status             | Score (1-5) | Notes                                  |
|---|---|---|--------------------|------------:|----------------------------------------|
| 1 | Tue Mar 10 | Java foundations | Done               |           5 | new shit                               |
| 2 | Wed Mar 11 | OOP fundamentals | Done               |           5 | took more than it needed but good shit |
| 3 | Thu Mar 12 | Collections + Generics | In Progress      |             |                                        |
| 4 | Fri Mar 13 | Exceptions + Strings + Immutability | Done most of it    |           3 |                                        |
| 5 | Sat Mar 14 | Lambdas + Streams + Optional | In Progress        |           3 |                                        |
| 6 | Sun Mar 15 | SQL essentials | Not Started        |             |                                        |
| 7 | Mon Mar 16 | Java mini app + revision | Done most of it    |             |                                        |
| 8 | Tue Mar 17 | Spring Boot basics | Not Started        |             |                                        |
| 9 | Wed Mar 18 | REST API design | Not Started        |             |                                        |
| 10 | Thu Mar 19 | Validation + error handling | Not Started        |             |                                        |
| 11 | Fri Mar 20 | JPA + transactions + performance | Not Started        |             |                                        |
| 12 | Sat Mar 21 | Security + JWT | Not Started        |             |                                        |
| 13 | Sun Mar 22 | Testing + Testcontainers | Not Started        |             |                                        |
| 14 | Mon Mar 23 | Mock interview + final polish | Not Started        |             |                                        |

---

## Day-by-Day Detailed Tracker

## Day 1 - Java foundations (Tue Mar 10)
- [x] JDK, VS Code, terminal checks done
- [x] Primitive types, variables, operators, conditionals, loops
- [x] Methods: parameters, return types, scope
- [x] Practice: calculator, even/odd, max-of-3
- [x] Interview drill: explain JDK vs JRE vs JVM in under 60 seconds
- Deliverable: `core-java/day1` code committed
- Reflection:
  - Wins: learned how operators work in java and the difference between list and array , input and output in cli, switch cases,   
  - Gaps: 

## Day 2 - OOP fundamentals (Wed Mar 11)
- [x] Classes, objects, constructors
- [x] Encapsulation, inheritance, polymorphism, abstraction
- [x] Interface vs abstract class
- [x] Practice: `Book`, `User`, `Task` domain model
- [x] Interview drill: overloading vs overriding
- Deliverable: model package with at least 3 entities
- Reflection:
  - Wins:
  - Gaps:

## Day 3 - Collections + Generics (Thu Mar 12)

### Learn — Collections Framework
- [ ] Understand the `Collection` interface hierarchy (draw a quick diagram)
- [ ] `List` deep-dive
  - [ ] `ArrayList` vs `LinkedList` — when to pick which and why
  - [ ] Practice: create, add, remove, iterate, sort a list of `Task` objects
- [ ] `Set` deep-dive
  - [ ] `HashSet` vs `LinkedHashSet` vs `TreeSet` — ordering guarantees
  - [ ] Practice: remove duplicates from a list of names using a `Set`
- [ ] `Map` deep-dive
  - [ ] `HashMap` internals: hashing, buckets, collision handling (chaining)
  - [ ] `LinkedHashMap` (insertion order) vs `TreeMap` (sorted keys)
  - [ ] Practice: word-frequency counter — count occurrences of each word in a sentence
  - [ ] Practice: group a list of students by grade using `Map<String, List<Student>>`
- [ ] `Queue` / `Deque` basics
  - [ ] FIFO with `LinkedList` as a Queue
  - [ ] Practice: simulate a print-job queue (add jobs, process in order)

### Learn — Generics
- [ ] Why generics exist (type safety vs raw types)
- [ ] Write a generic `Pair<K, V>` class
- [ ] Understand bounded types: `<T extends Comparable<T>>`
- [ ] Wildcards overview: `<?>`, `<? extends T>`, `<? super T>`

### Learn — `equals()` and `hashCode()`
- [ ] The contract: if `a.equals(b)` then `a.hashCode() == b.hashCode()`
- [ ] Override both in your `Task` or `Book` class
- [ ] Test: add objects to a `HashSet` and verify deduplication works correctly

### Code exercises
- [ ] Frequency counter (count character occurrences in a string)
- [ ] Deduplicate a `List<String>` preserving insertion order
- [ ] Group a list of items by a key (e.g., tasks by status)
- [ ] Find the first non-repeating character in a string using `LinkedHashMap`
- [ ] Merge two maps summing values for duplicate keys

### Interview drill (speak aloud, 2 min each)
- [ ] "When would you use a `List` vs a `Set`?"
- [ ] "How does `HashMap` store and retrieve elements internally?"
- [ ] "What happens if you forget to override `hashCode()`?"

- Deliverable: `core-java/day3` — all exercises committed
- Reflection:
  - Wins:
  - Gaps:

## Day 4 - Exceptions + Strings + Immutability (Fri Mar 13)

### Learn — Exception handling
- [ ] Checked vs unchecked exceptions
  - [ ] Draw the hierarchy: `Throwable` → `Exception` (checked) / `RuntimeException` (unchecked)
  - [ ] List 3 common checked (`IOException`, `SQLException`, `FileNotFoundException`)
  - [ ] List 3 common unchecked (`NullPointerException`, `IllegalArgumentException`, `IndexOutOfBoundsException`)
- [x] `try/catch/finally`, `throw`, custom exception
- [ ] `try-with-resources` — write an example that reads a file and auto-closes the stream
- [ ] Reading stack traces and debugging
  - [ ] Intentionally cause a `NullPointerException` and trace it back to the root cause
  - [ ] Practice reading a 3-level-deep stack trace (method A calls B calls C)
- [ ] Create a custom `InvalidTaskException` with a message and error code field
- [ ] Understand when to catch vs when to propagate exceptions

### Learn — Strings
- [ ] `String` immutability — why `"abc" + "def"` creates new objects
- [ ] `String` pool and `intern()` concept
- [ ] `StringBuilder` vs `StringBuffer` — when to use which
- [ ] Practice: build a CSV line from an array of values using `StringBuilder`
- [ ] Common `String` methods drill: `substring`, `charAt`, `indexOf`, `split`, `trim`, `replace`
- [ ] Practice: reverse a string without using library methods
- [ ] Practice: check if a string is a palindrome

### Learn — Immutability
- [ ] What makes an object immutable (private final fields, no setters, defensive copies)
- [ ] Write an immutable `Money` class with `amount` and `currency`
- [ ] Explain why immutability helps in multi-threaded code (conceptual)

### Code exercises
- [ ] Robust input parser: read user input, validate it's a positive integer, throw custom exception if not
- [ ] CLI menu that catches bad input gracefully and re-prompts instead of crashing
- [ ] String manipulation: count vowels, capitalize first letter of each word, remove extra whitespace
- [ ] Parse a date string (`"2026-03-13"`) and handle `DateTimeParseException`

### Interview drill (speak aloud, 2 min each)
- [ ] "What's the difference between checked and unchecked exceptions?"
- [ ] "Why is `String` immutable in Java?"
- [ ] "When would you create a custom exception?"

- Deliverable: `core-java/day4` — exception-safe CLI flow + string exercises committed
- Reflection:
  - Wins:
  - Gaps:

## Day 5 - Lambdas + Streams + Optional (Sat Mar 14)

### Learn — Lambdas & functional interfaces
- [ ] Understand what a functional interface is (`@FunctionalInterface`)
- [ ] Core interfaces: `Predicate<T>`, `Function<T,R>`, `Consumer<T>`, `Supplier<T>`
- [ ] Write a lambda for each of the 4 core interfaces with a real example
- [ ] Method references: `Class::method` — rewrite at least 2 lambdas as method references
- [ ] Practice: sort a list of `Task` objects by due date using `Comparator` lambda

### Learn — Stream API
- [ ] How to create a stream: `.stream()`, `Stream.of()`, `Arrays.stream()`
- [ ] Intermediate operations (lazy): `filter`, `map`, `flatMap`, `sorted`, `distinct`, `peek`
- [ ] Terminal operations (trigger execution): `collect`, `forEach`, `reduce`, `count`, `findFirst`, `anyMatch`
- [ ] Collectors: `toList()`, `toSet()`, `toMap()`, `groupingBy()`, `joining()`
- [ ] Practice exercises:
  - [ ] Filter a list of tasks to only "IN_PROGRESS" ones
  - [ ] Map a list of users to their email addresses
  - [ ] Sort tasks by priority descending, then by title alphabetically
  - [ ] Use `reduce` to sum the total estimated hours across all tasks
  - [ ] Use `groupingBy` to group tasks by status
- [ ] Understand when streams are overkill vs when they shine

### Learn — Optional
- [ ] Why `Optional` exists (avoid `NullPointerException`)
- [ ] Creating: `Optional.of()`, `Optional.ofNullable()`, `Optional.empty()`
- [ ] Using: `isPresent()`, `ifPresent()`, `orElse()`, `orElseThrow()`, `map()`, `flatMap()`
- [ ] Anti-patterns: never use `Optional` as a method parameter or field
- [ ] Practice: write a `findTaskById()` method that returns `Optional<Task>`
- [ ] Practice: chain `Optional` with `.map().orElse()` to safely get a task's assignee name

### Code exercises (stream-based solutions)
- [ ] Given a list of strings, return a comma-separated string of those longer than 5 chars, uppercased
- [ ] Find the 3 most expensive items from a product list
- [ ] Flatten a `List<List<String>>` into a single list of unique sorted strings
- [ ] Count how many tasks each user is assigned to (return `Map<String, Long>`)
- [ ] Find the first task that is overdue, or return a default "no overdue tasks" message

### Interview drill (speak aloud, 2 min each)
- [ ] "What's the difference between `map` and `flatMap`?"
- [ ] "When would you prefer a loop over a stream?"
- [ ] "How does `Optional` help prevent null pointer exceptions?"

- Deliverable: `core-java/day5` — stream-based solutions for all exercises committed
- Reflection:
  - Wins:
  - Gaps:

## Day 6 - SQL essentials (Sun Mar 15)

### Setup
- [ ] Create a sample database `prep_db` in PostgreSQL
- [ ] Create tables: `users(id, name, email, created_at)`, `tasks(id, title, status, priority, user_id, created_at)`
- [ ] Insert at least 15 sample rows into each table

### Learn — Basic queries
- [ ] `SELECT` specific columns vs `SELECT *` (why `*` is bad in production)
- [ ] `WHERE` with comparison operators (`=`, `!=`, `>`, `<`, `BETWEEN`, `IN`, `LIKE`)
- [ ] `ORDER BY` ascending/descending, multi-column sort
- [ ] `LIMIT` and `OFFSET` for pagination
- [ ] `DISTINCT` to remove duplicates

### Learn — Aggregation
- [ ] Aggregate functions: `COUNT`, `SUM`, `AVG`, `MIN`, `MAX`
- [ ] `GROUP BY` — count tasks per user, average priority per status
- [ ] `HAVING` — filter groups (e.g., users with more than 3 tasks)

### Learn — Joins
- [ ] `INNER JOIN` — get tasks with their user names
- [ ] `LEFT JOIN` — get all users including those with zero tasks
- [ ] `RIGHT JOIN` / `FULL OUTER JOIN` — understand conceptually
- [ ] Self-join concept (e.g., manager-employee relationship)

### Learn — Indexing
- [ ] What an index is (B-tree concept, high level)
- [ ] When indexes help (frequent `WHERE`/`JOIN` columns) vs when they hurt (write-heavy tables)
- [ ] Create an index on `tasks.user_id` and `tasks.status`
- [ ] Use `EXPLAIN` to see query plan before and after adding an index

### Learn — Normalization
- [ ] 1NF, 2NF, 3NF — explain each with a concrete example
- [ ] When denormalization is acceptable (read-heavy analytics)

### Learn — Transactions & isolation
- [ ] ACID properties (define each in one sentence)
- [ ] `BEGIN`, `COMMIT`, `ROLLBACK` — run a manual transaction in psql
- [ ] Isolation levels overview: Read Uncommitted → Read Committed → Repeatable Read → Serializable
- [ ] Dirty read, non-repeatable read, phantom read — what each means

### Practice queries (10+)
- [ ] List all tasks for a specific user
- [ ] Count tasks grouped by status
- [ ] Find users who have no tasks (`LEFT JOIN ... WHERE task.id IS NULL`)
- [ ] Get top 5 users by task count
- [ ] Find tasks created in the last 7 days
- [ ] Update all "PENDING" tasks to "IN_PROGRESS" for a specific user (in a transaction)
- [ ] Delete tasks older than 30 days (with `RETURNING` to see deleted rows)
- [ ] Find the user with the highest average task priority
- [ ] Subquery: find users whose task count is above average
- [ ] Write a paginated query: page 2, page size 5, ordered by created_at

### Interview drill (speak aloud, 2 min each)
- [ ] "What is a SQL index and when would you not use one?"
- [ ] "Explain the difference between `INNER JOIN` and `LEFT JOIN`."
- [ ] "What does ACID stand for?"

- Deliverable: `sql/day6.sql` — all queries saved and commented
- Reflection:
  - Wins:
  - Gaps:

## Day 7 - Java mini app + revision (Mon Mar 16)

### Build — Console Task Tracker app
- [ ] **Project setup**
  - [ ] Create package structure: `model`, `service`, `util`, `exception`, `app`
  - [ ] Entry point: `TaskTrackerApp.java` with a CLI menu loop
- [ ] **Model layer**
  - [ ] `Task` class with fields: `id`, `title`, `description`, `status` (enum: TODO, IN_PROGRESS, DONE), `priority`, `createdAt`
  - [ ] `TaskStatus` enum
  - [ ] Override `equals()`, `hashCode()`, `toString()` on `Task`
- [ ] **Service layer**
  - [ ] `TaskService` with methods: `addTask()`, `removeTask()`, `updateStatus()`, `listAll()`, `filterByStatus()`, `searchByTitle()`
  - [ ] Store tasks in a `Map<Integer, Task>` (in-memory)
  - [ ] Use `Optional` for `findById()`
  - [ ] Throw `InvalidTaskException` for bad input
- [ ] **Features to implement**
  - [ ] Add a new task (validate title not empty)
  - [ ] List all tasks (formatted table output)
  - [ ] Filter tasks by status using streams
  - [ ] Mark a task as done
  - [ ] Delete a task by ID
  - [ ] Search tasks by keyword in title (case-insensitive, using streams)
  - [ ] Show task count summary per status using `groupingBy`
- [ ] **Error handling**
  - [ ] Graceful handling of bad menu input (catch `NumberFormatException`)
  - [ ] Custom exception for task-not-found scenarios
  - [ ] Never let the app crash from user input
- [ ] **Code quality**
  - [ ] No method longer than 20 lines
  - [ ] Meaningful variable names
  - [ ] Comments on non-obvious logic only

### Revision — Week 1 gap review
- [ ] Re-do 1 exercise from Day 3 (collections) that felt weak
- [ ] Re-do 1 exercise from Day 4 (exceptions/strings) that felt weak
- [ ] Re-do 1 exercise from Day 5 (streams) that felt weak
- [ ] Review all Day 1–6 interview drill questions — can you answer each in under 2 minutes?

### Solve 10 mixed Java problems
- [ ] Reverse an array in place
- [ ] Check if two strings are anagrams
- [ ] Find the second largest number in an array
- [ ] Remove duplicates from a sorted array
- [ ] Implement a basic stack using `ArrayList`
- [ ] FizzBuzz (classic)
- [ ] Find all pairs in an array that sum to a target
- [ ] Rotate an array by K positions
- [ ] Check for balanced parentheses using a stack
- [ ] Convert a Roman numeral string to an integer

### Week 1 self-assessment
- [ ] List my top 3 weak areas from this week
- [ ] For each weak area, write one sentence on how I'll address it
- [ ] Rate my confidence (1-5) on: OOP, Collections, Exceptions, Streams, SQL

- Deliverable: runnable mini app in `core-java/day7` + revision notes committed
- Reflection:
  - Wins:
  - Gaps:

## Day 8 - Spring Boot basics (Tue Mar 17)

### Learn — Spring framework concepts
- [ ] What is Spring? What problem does it solve?
- [ ] Spring vs Spring Boot — why Boot exists (auto-configuration, starter dependencies, embedded server)
- [ ] Dependency Injection (DI) and Inversion of Control (IoC)
  - [ ] Explain DI in one sentence with a real example
  - [ ] Constructor injection vs field injection — why constructor is preferred
- [ ] Bean lifecycle: what is a bean, `@Component`, `@Service`, `@Repository`, `@Controller`
- [ ] `@Autowired` — how Spring resolves dependencies
- [ ] `application.properties` / `application.yml` — how config works

### Hands-on — Project setup
- [ ] Go to [start.spring.io](https://start.spring.io) and generate a project
  - [ ] Dependencies: Spring Web, Spring Data JPA, PostgreSQL Driver, Validation, Spring Security, Lombok
- [ ] Import into VS Code and understand the folder structure
- [ ] Identify: `@SpringBootApplication`, `main()` method, `application.properties`
- [ ] Run the app (`mvn spring-boot:run`) and confirm it starts on port 8080
- [ ] Create a simple `GET /api/health` endpoint that returns `{"status": "UP"}`
- [ ] Understand the 3-layer architecture:
  - [ ] `controller` — handles HTTP requests
  - [ ] `service` — business logic
  - [ ] `repository` — database access
  - [ ] Draw a diagram of how a request flows through these layers

### Learn — Lombok
- [ ] `@Data`, `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`
- [ ] Why Lombok reduces boilerplate (but know what it generates)

### Interview drill (speak aloud, 2 min each)
- [ ] "What is Dependency Injection and why is it useful?"
- [ ] "What's the difference between `@Component`, `@Service`, and `@Repository`?"
- [ ] "Explain how Spring Boot auto-configuration works at a high level."

- Deliverable: Spring Boot app starts locally with a health endpoint
- Reflection:
  - Wins:
  - Gaps:

## Day 9 - REST API design (Wed Mar 18)

### Learn — REST principles
- [ ] What REST is (resources, stateless, uniform interface)
- [ ] HTTP methods and their meaning: `GET`, `POST`, `PUT`, `PATCH`, `DELETE`
- [ ] Idempotency — which methods are idempotent and why it matters
- [ ] URL naming conventions: `/api/tasks`, `/api/tasks/{id}`, `/api/users/{id}/tasks`

### Hands-on — Build Task CRUD
- [ ] Create `TaskController` with `@RestController` and `@RequestMapping("/api/tasks")`
- [ ] Create `TaskService` interface and `TaskServiceImpl`
- [ ] Create `TaskRequest` DTO (what the client sends) and `TaskResponse` DTO (what the API returns)
- [ ] Implement endpoints:
  - [ ] `POST /api/tasks` — create a task, return `201 Created`
  - [ ] `GET /api/tasks` — list all tasks, return `200 OK`
  - [ ] `GET /api/tasks/{id}` — get one task, return `200 OK` or `404 Not Found`
  - [ ] `PUT /api/tasks/{id}` — update a task, return `200 OK`
  - [ ] `DELETE /api/tasks/{id}` — delete a task, return `204 No Content`
- [ ] Use `@PathVariable`, `@RequestParam`, `@RequestBody` correctly
- [ ] Return proper `ResponseEntity<>` with status codes
- [ ] Test every endpoint with Postman or curl

### Learn — DTO pattern
- [ ] Why you should never expose entities directly in the API
- [ ] Manual mapping vs MapStruct (understand both, use manual for now)
- [ ] Create a `TaskMapper` utility class to convert between entity and DTO

### Learn — `@ResponseStatus` and `ResponseEntity`
- [ ] When to use `@ResponseStatus` on exception classes
- [ ] When to use `ResponseEntity.ok()`, `.created()`, `.noContent()`, `.notFound()`

### Interview drill (speak aloud, 2 min each)
- [ ] "Walk me through the request lifecycle in Spring Boot."
- [ ] "What's the difference between `PUT` and `PATCH`?"
- [ ] "Why do we use DTOs instead of exposing entities?"

- Deliverable: Full Task CRUD working and tested via Postman
- Reflection:
  - Wins:
  - Gaps:

## Day 10 - Validation + error handling (Thu Mar 19)

### Learn — Bean validation
- [ ] Add `spring-boot-starter-validation` dependency
- [ ] Annotate DTO fields: `@NotNull`, `@NotBlank`, `@Size`, `@Email`, `@Min`, `@Max`, `@Pattern`
- [ ] Use `@Valid` on controller method parameters to trigger validation
- [ ] Understand `BindingResult` — how Spring collects validation errors
- [ ] Create custom validation annotation (e.g., `@ValidTaskStatus`) — bonus

### Hands-on — Global exception handling
- [ ] Create `ApiErrorResponse` class with fields: `timestamp`, `status`, `error`, `message`, `path`
- [ ] Create `GlobalExceptionHandler` with `@ControllerAdvice`
  - [ ] Handle `MethodArgumentNotValidException` — return `400` with field-level error details
  - [ ] Handle `ResourceNotFoundException` (custom) — return `404`
  - [ ] Handle `DataIntegrityViolationException` — return `409 Conflict`
  - [ ] Handle generic `Exception` — return `500` with safe message (never leak stack traces)
- [ ] Test: send invalid data and verify the error response format is consistent

### Hands-on — Pagination and filtering
- [ ] Use `Pageable` parameter in repository and controller
- [ ] Accept `?page=0&size=10&sort=createdAt,desc` query params
- [ ] Return `Page<TaskResponse>` with metadata (totalElements, totalPages, current page)
- [ ] Add filtering: `?status=IN_PROGRESS` and `?search=keyword`
- [ ] Understand `Specification` or simple query method approach

### Interview drill (speak aloud, 2 min each)
- [ ] "How does Spring Boot handle validation errors?"
- [ ] "What is `@ControllerAdvice` and why is it useful?"
- [ ] "How would you implement pagination in a REST API?"

- Deliverable: validation errors return clean JSON, pagination works on list endpoint
- Reflection:
  - Wins:
  - Gaps:

## Day 11 - JPA + transactions + performance (Fri Mar 20)

### Learn — JPA entity mapping
- [ ] `@Entity`, `@Table`, `@Id`, `@GeneratedValue` basics
- [ ] Column mapping: `@Column(nullable = false, length = 100)`
- [ ] Relationships:
  - [ ] `@ManyToOne` / `@OneToMany` — User has many Tasks
  - [ ] `@JoinColumn` — foreign key mapping
  - [ ] `@OneToOne` — e.g., User has one Profile (conceptual)
  - [ ] `@ManyToMany` — e.g., Task has many Tags (conceptual)
- [ ] `@Enumerated(EnumType.STRING)` for status fields
- [ ] Audit fields: `@CreatedDate`, `@LastModifiedDate` with `@EntityListeners`

### Hands-on — Repository layer
- [ ] Create `TaskRepository extends JpaRepository<Task, Long>`
- [ ] Use built-in methods: `save()`, `findById()`, `findAll()`, `deleteById()`
- [ ] Write custom query methods: `findByStatus()`, `findByTitleContainingIgnoreCase()`
- [ ] Write a `@Query` with JPQL: find tasks by user ID ordered by created date
- [ ] Connect controller → service → repository → PostgreSQL end-to-end
- [ ] Verify data persists in the database using `psql` or pgAdmin

### Learn — Transactions
- [ ] What `@Transactional` does: begin → execute → commit/rollback
- [ ] Where to place it: service layer (not controller, not repository)
- [ ] Read-only transactions: `@Transactional(readOnly = true)` for queries
- [ ] Rollback rules: rolls back on unchecked exceptions by default
- [ ] Practice: create a service method that saves two entities — simulate a failure after the first save, verify rollback

### Learn — Performance awareness
- [ ] Lazy vs eager loading: `FetchType.LAZY` (default for collections) vs `FetchType.EAGER`
- [ ] The N+1 problem: what it is and how to spot it
  - [ ] Enable SQL logging: `spring.jpa.show-sql=true`
  - [ ] Trigger an N+1 query and observe the SQL output
- [ ] Fix with `JOIN FETCH` in JPQL or `@EntityGraph`
- [ ] Know about `spring.jpa.open-in-view=false` and why it's recommended

### Learn — Database migrations
- [ ] Why migrations matter (version control for your schema)
- [ ] Set up Flyway: add dependency, create `db/migration/V1__create_tables.sql`
- [ ] Run the app and verify Flyway applies the migration
- [ ] Create a second migration: `V2__add_priority_column.sql`

### Interview drill (speak aloud, 2 min each)
- [ ] "What is the N+1 query problem and how do you fix it?"
- [ ] "What does `@Transactional` do and where should you place it?"
- [ ] "Explain lazy vs eager loading."

- Deliverable: entities persisted in PostgreSQL with relationships, Flyway migrations running
- Reflection:
  - Wins:
  - Gaps:

## Day 12 - Security + JWT (Sat Mar 21)

### Learn — Security concepts
- [ ] Authentication (who are you?) vs Authorization (what can you do?)
- [ ] Session-based auth vs token-based auth — why tokens win for APIs
- [ ] JWT structure: Header.Payload.Signature — decode one at [jwt.io](https://jwt.io)
- [ ] JWT flow: login → receive token → send token in `Authorization: Bearer <token>` header

### Learn — Spring Security basics
- [ ] How the filter chain works (high level — don't go too deep)
- [ ] `SecurityFilterChain` bean configuration
- [ ] Disabling CSRF for stateless APIs (and why it's safe)
- [ ] `UserDetailsService` — how Spring loads user data
- [ ] `PasswordEncoder` — use `BCryptPasswordEncoder`, never store plain passwords

### Hands-on — Implement auth
- [ ] Create `User` entity with fields: `id`, `email`, `password`, `role` (enum: USER, ADMIN)
- [ ] Create `AuthController` with:
  - [ ] `POST /api/auth/register` — hash password, save user, return success
  - [ ] `POST /api/auth/login` — validate credentials, return JWT
- [ ] Create `JwtService` — generate token, validate token, extract claims
- [ ] Create `JwtAuthenticationFilter` — intercept requests, extract token, set authentication
- [ ] Configure `SecurityFilterChain`:
  - [ ] Permit: `/api/auth/**`
  - [ ] Require auth: everything else
- [ ] Role-based access: `@PreAuthorize("hasRole('ADMIN')")` on admin-only endpoints

### Test the flow
- [ ] Register a new user via Postman
- [ ] Login and receive a JWT
- [ ] Access a protected endpoint without token → `401 Unauthorized`
- [ ] Access with valid token → `200 OK`
- [ ] Access admin endpoint with USER role → `403 Forbidden`

### Interview drill (speak aloud, 2 min each)
- [ ] "How does JWT authentication work in a stateless API?"
- [ ] "What's the difference between authentication and authorization?"
- [ ] "Why do we hash passwords instead of encrypting them?"

- Deliverable: login/register working, protected endpoints with JWT + role-based access
- Reflection:
  - Wins:
  - Gaps:

## Day 13 - Testing + Testcontainers (Sun Mar 22)

### Learn — Testing concepts
- [ ] Test pyramid: unit tests (many) → integration tests (some) → E2E tests (few)
- [ ] What to test vs what not to test
- [ ] Arrange-Act-Assert pattern
- [ ] Test naming convention: `should_returnTask_when_validIdProvided()`

### Hands-on — Unit tests (JUnit 5 + Mockito)
- [ ] Add `spring-boot-starter-test` (already included by default)
- [ ] Write unit tests for `TaskService`:
  - [ ] `should_createTask_when_validInput()`
  - [ ] `should_throwException_when_taskNotFound()`
  - [ ] `should_returnFilteredTasks_when_statusProvided()`
- [ ] Use `@Mock` and `@InjectMocks` to isolate the service from the repository
- [ ] Use `when().thenReturn()` and `verify()` patterns
- [ ] Use `assertThrows()` for exception cases

### Hands-on — Web layer tests
- [ ] Use `@WebMvcTest(TaskController.class)`
- [ ] Use `MockMvc` to simulate HTTP requests
- [ ] Test: `GET /api/tasks/{id}` returns `200` with valid data
- [ ] Test: `POST /api/tasks` with invalid body returns `400` with error details
- [ ] Test: `GET /api/tasks/{id}` for non-existent ID returns `404`

### Hands-on — Integration tests
- [ ] Use `@SpringBootTest` with `@AutoConfigureMockMvc`
- [ ] Test the full request flow: controller → service → repository → DB
- [ ] Use Testcontainers to spin up a real PostgreSQL container for tests
  - [ ] Add `testcontainers` and `postgresql` test dependencies
  - [ ] Create `@TestConfiguration` with `@Container` PostgreSQL setup
  - [ ] Or use `@ServiceConnection` (Spring Boot 3.1+) for simpler setup
- [ ] Write at least 2 integration tests:
  - [ ] Create a task and then retrieve it
  - [ ] Delete a task and verify it's gone

### Code coverage
- [ ] Run tests: `mvn test`
- [ ] All tests pass green
- [ ] Aim for at least 60-70% coverage on service layer

### Interview drill (speak aloud, 2 min each)
- [ ] "What's the difference between a unit test and an integration test?"
- [ ] "What is Mockito and when do you use it?"
- [ ] "Why use Testcontainers instead of H2 for integration tests?"

- Deliverable: test suite runs green with unit + integration tests
- Reflection:
  - Wins:
  - Gaps:

## Day 14 - Mock interview + final polish (Mon Mar 23)

### Project polish
- [ ] Clean up all `TODO` comments in code
- [ ] Consistent code formatting (run formatter)
- [ ] Check no sensitive data in properties (use env variables for DB creds)
- [ ] Write a proper `README.md`:
  - [ ] Project description (2-3 sentences)
  - [ ] Tech stack list
  - [ ] Architecture diagram (text-based is fine)
  - [ ] How to run locally (prerequisites, commands)
  - [ ] API endpoint summary table
  - [ ] How to run tests
- [ ] Verify the app runs cleanly from a fresh `git clone` → `mvn spring-boot:run`

### Mock interview — project walkthrough
- [ ] Practice your 2-3 minute project pitch (record yourself and listen back)
  - [ ] What the project does
  - [ ] Tech stack choices and why
  - [ ] Architecture: controller → service → repository → DB
  - [ ] One challenge you overcame and what you learned
- [ ] Be ready to explain any file in your codebase if asked
- [ ] Be ready to live-demo: create user → login → create task → list tasks

### Mock interview — technical questions
- [ ] Review all interview questions from the checklist below
- [ ] For each, answer aloud in under 2 minutes
- [ ] Record your weakest 5 answers and re-practice those
- [ ] Practice "I don't know" responses — say what you *do* know and how you'd learn the rest

### Mock interview — behavioral questions
- [ ] "Tell me about yourself" — 90-second version focused on tech journey
- [ ] "Why do you want this training/role?"
- [ ] "Describe a time you were stuck on a bug and how you resolved it."
- [ ] "How do you approach learning a new technology?"
- [ ] "What would you do if you disagreed with a senior developer's approach?"

### Final self-assessment
- [ ] Rate confidence (1-5) on each topic: Core Java, OOP, Collections, Streams, SQL, Spring Boot, REST, JPA, Security, Testing
- [ ] List top 3 strengths to highlight in the interview
- [ ] List top 3 gaps and brief talking points if they come up

- Deliverable: interview-ready project + polished README + practiced answers
- Reflection:
  - Wins:
  - Gaps:

---

## Target Project Checklist (Task Management API)

### Core features
- [ ] User registration and login (`/api/auth/register`, `/api/auth/login`)
- [ ] JWT-based auth with token generation and validation
- [ ] Role-based authorization (USER, ADMIN)
- [ ] Task CRUD with proper HTTP methods and status codes
- [ ] Assign tasks to users (relationship)
- [ ] Pagination (`?page=0&size=10`) and filtering (`?status=`, `?search=`)

### Code quality
- [ ] Input validation with Bean Validation annotations
- [ ] Global exception handling with `@ControllerAdvice`
- [ ] Consistent API error response format
- [ ] DTO pattern — never expose entities in API
- [ ] Service layer contains all business logic (controllers stay thin)

### Data layer
- [ ] PostgreSQL integration with Spring Data JPA
- [ ] Entity relationships mapped correctly (`@ManyToOne`, `@OneToMany`)
- [ ] DB migration tool (Flyway) with at least 2 migration scripts
- [ ] `@Transactional` on service methods

### Testing
- [ ] Unit tests for service layer (JUnit 5 + Mockito)
- [ ] Web layer tests (`@WebMvcTest` + MockMvc)
- [ ] Integration tests (`@SpringBootTest` + Testcontainers)

### Documentation
- [ ] README with: description, tech stack, architecture, setup instructions, API endpoints, how to test

---

## Interview Questions Checklist

### Core Java
- [ ] Difference between JDK, JRE, JVM
- [ ] Overloading vs overriding
- [ ] Interface vs abstract class
- [ ] `==` vs `.equals()`
- [ ] Why `hashCode()` matters in maps/sets
- [ ] Checked vs unchecked exceptions
- [ ] `final`, `static`, and immutability
- [ ] Stream API benefits and pitfalls
- [ ] Access modifiers: `public`, `protected`, default, `private`
- [ ] Wrapper classes and autoboxing (`int` vs `Integer`)

### OOP & Design Principles
- [ ] SOLID principles — explain each in one sentence with an example
  - [ ] **S**ingle Responsibility: one class = one job
  - [ ] **O**pen/Closed: open for extension, closed for modification
  - [ ] **L**iskov Substitution: subtypes must be substitutable for base types
  - [ ] **I**nterface Segregation: prefer small interfaces over fat ones
  - [ ] **D**ependency Inversion: depend on abstractions, not concretions
- [ ] Common design patterns (know at least 3):
  - [ ] Singleton — and why Spring beans are singletons by default
  - [ ] Factory — creating objects without specifying exact class
  - [ ] Builder — used by Lombok `@Builder`, why it helps with many constructor params
  - [ ] Strategy — similar to using interfaces for interchangeable behavior

### Spring Boot
- [ ] What problem does Spring Boot solve
- [ ] Dependency Injection in simple words
- [ ] `@Controller` vs `@RestController`
- [ ] Role of `@Service` and `@Repository`
- [ ] How auto-configuration works (high level)
- [ ] Why DTOs are used instead of entities in API
- [ ] Bean scopes: singleton (default) vs prototype
- [ ] `@Configuration` and `@Bean` — when you need manual bean registration
- [ ] Spring profiles (`application-dev.yml`, `application-prod.yml`)

### JPA + Database
- [ ] Entity lifecycle basics
- [ ] Lazy vs eager loading
- [ ] What `@Transactional` does
- [ ] N+1 query problem
- [ ] Index basics and tradeoffs
- [ ] ACID and isolation level concepts
- [ ] Difference between `save()` and `saveAndFlush()`
- [ ] What Flyway/Liquibase does and why it matters

### API + Testing
- [ ] Idempotency (`PUT` vs `POST`)
- [ ] Common status codes (200, 201, 204, 400, 401, 403, 404, 409, 500)
- [ ] Unit test vs integration test
- [ ] Why MockMvc is useful
- [ ] Why Testcontainers helps reliability
- [ ] What `@WebMvcTest` vs `@SpringBootTest` loads

### Behavioral (practice aloud)
- [ ] Tell me about yourself (90-second tech-focused pitch)
- [ ] Why this role / company?
- [ ] Describe a challenging bug you solved
- [ ] How do you learn new technologies?
- [ ] How do you handle code reviews or disagreements?

---

## Weekly Gate Checks

### End of Week 1 (after Day 7)
- [ ] I can solve basic-to-intermediate Java problems without heavy lookup
- [ ] I can explain OOP, collections, and exceptions clearly in under 2 minutes each
- [ ] I can write clean methods with proper error handling
- [ ] I can use streams and lambdas to transform and filter data
- [ ] I can write basic-to-intermediate SQL queries including joins
- [ ] I built a working console app that uses OOP + collections + exceptions + streams

### End of Week 2 (after Day 14)
- [ ] I built and tested a complete Spring Boot REST API with PostgreSQL
- [ ] I can explain the full request flow: HTTP → controller → service → repository → DB → response
- [ ] I can explain SOLID principles and identify 3-4 design patterns in my code
- [ ] I can defend every design choice: "I chose X because..."
- [ ] My project runs from a fresh clone and all tests pass
- [ ] I can do a 2-3 minute project walkthrough without stumbling

---

## Daily Log Template (Copy for each day)

Date:

Completed:
- 

Blocked on:
- 

Fix plan for tomorrow:
- 

One interview answer I improved today:
- 

---

## Recommended Resources

### Core Java
- [Baeldung Java tutorials](https://www.baeldung.com/java-tutorial) — concise and practical
- [Java Brains (YouTube)](https://www.youtube.com/c/JavaBrains) — great for visual learners

### Spring Boot
- [Spring Boot official guides](https://spring.io/guides) — short task-focused tutorials
- [Amigoscode Spring Boot (YouTube)](https://www.youtube.com/c/amigoscode) — project-based
- [Baeldung Spring Boot](https://www.baeldung.com/spring-boot) — reference-style

### SQL
- [SQLBolt](https://sqlbolt.com/) — interactive SQL exercises
- [PostgreSQL official docs](https://www.postgresql.org/docs/) — when you need specifics

### Interview prep
- [Java Interview Guide — Baeldung](https://www.baeldung.com/java-interview-questions)
- Practice explaining out loud to a mirror, pet, or recording — hearing yourself reveals gaps
