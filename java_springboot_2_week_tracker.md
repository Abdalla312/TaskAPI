# Java + Spring Boot 2-Week Interview Prep Tracker

Period: March 10, 2026 to March 23, 2026

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
- [ x ] `java -version` works
- [ x ] `javac -version` works
- [ x ] `mvn -v` works
- [ x ] VS Code Java extensions installed
- [ x ] PostgreSQL installed and reachable
- [ ] Git repo initialized for your practice project

## Progress Dashboard

Use `Not Started`, `In Progress`, or `Done` in the Status column.

| Day | Date | Focus | Status | Score (1-5) | Notes |
|---|---|---|---|---:|---|
| 1 | Tue Mar 10 | Java foundations | In Progress  |  |  |
| 2 | Wed Mar 11 | OOP fundamentals | Not Started |  |  |
| 3 | Thu Mar 12 | Collections + Generics | Not Started |  |  |
| 4 | Fri Mar 13 | Exceptions + Strings + Immutability | Not Started |  |  |
| 5 | Sat Mar 14 | Lambdas + Streams + Optional | Not Started |  |  |
| 6 | Sun Mar 15 | SQL essentials | Not Started |  |  |
| 7 | Mon Mar 16 | Java mini app + revision | Not Started |  |  |
| 8 | Tue Mar 17 | Spring Boot basics | Not Started |  |  |
| 9 | Wed Mar 18 | REST API design | Not Started |  |  |
| 10 | Thu Mar 19 | Validation + error handling | Not Started |  |  |
| 11 | Fri Mar 20 | JPA + transactions + performance | Not Started |  |  |
| 12 | Sat Mar 21 | Security + JWT | Not Started |  |  |
| 13 | Sun Mar 22 | Testing + Testcontainers | Not Started |  |  |
| 14 | Mon Mar 23 | Mock interview + final polish | Not Started |  |  |

---

## Day-by-Day Detailed Tracker

## Day 1 - Java foundations (Tue Mar 10)
- [ ] JDK, VS Code, terminal checks done
- [ ] Primitive types, variables, operators, conditionals, loops
- [ ] Methods: parameters, return types, scope
- [ ] Practice: calculator, even/odd, max-of-3
- [ ] Interview drill: explain JDK vs JRE vs JVM in under 60 seconds
- Deliverable: `core-java/day1` code committed
- Reflection:
  - Wins:
  - Gaps:

## Day 2 - OOP fundamentals (Wed Mar 11)
- [ ] Classes, objects, constructors
- [ ] Encapsulation, inheritance, polymorphism, abstraction
- [ ] Interface vs abstract class
- [ ] Practice: `Book`, `User`, `Task` domain model
- [ ] Interview drill: overloading vs overriding
- Deliverable: model package with at least 3 entities
- Reflection:
  - Wins:
  - Gaps:

## Day 3 - Collections + Generics (Thu Mar 12)
- [ ] `List`, `Set`, `Map`, `Queue` use-cases
- [ ] Generics and type safety
- [ ] `equals()` and `hashCode()` contract
- [ ] HashMap basics (hashing, collision concept)
- [ ] Practice: frequency counter, deduplicate list, group by key
- Deliverable: `collections` exercises solved
- Reflection:
  - Wins:
  - Gaps:

## Day 4 - Exceptions + Strings + Immutability (Fri Mar 13)
- [ ] Checked vs unchecked exceptions
- [ ] `try/catch/finally`, `throw`, custom exception
- [ ] Reading stack traces and debugging
- [ ] `String` vs `StringBuilder`, immutable objects
- [ ] Practice: robust input parser with validation
- Deliverable: exception-safe CLI flow
- Reflection:
  - Wins:
  - Gaps:

## Day 5 - Lambdas + Streams + Optional (Sat Mar 14)
- [ ] Lambda syntax and functional interfaces
- [ ] Stream operations: `map`, `filter`, `sorted`, `collect`
- [ ] `Optional` best practices
- [ ] Practice: transform and aggregate task list
- [ ] Interview drill: loop vs stream tradeoffs
- Deliverable: stream-based solutions for 5 problems
- Reflection:
  - Wins:
  - Gaps:

## Day 6 - SQL essentials (Sun Mar 15)
- [ ] `SELECT`, `WHERE`, `ORDER BY`, `GROUP BY`
- [ ] Joins: `INNER`, `LEFT`
- [ ] Index basics and when they help
- [ ] Normalization basics
- [ ] Transactions and isolation level concept
- [ ] Practice: 10 SQL queries on sample schema
- Deliverable: saved `.sql` script with answers
- Reflection:
  - Wins:
  - Gaps:

## Day 7 - Java mini app + revision (Mon Mar 16)
- [ ] Build a small console app (Task tracker)
- [ ] Clear package structure and clean methods
- [ ] Apply OOP + collections + exceptions
- [ ] Solve 10 mixed Java interview problems
- [ ] List top 3 weak areas from Week 1
- Deliverable: runnable mini app
- Reflection:
  - Wins:
  - Gaps:

## Day 8 - Spring Boot basics (Tue Mar 17)
- [ ] Understand Spring vs Spring Boot
- [ ] DI and IoC basics
- [ ] Create project from Spring Initializr
- [ ] Run first app and create health endpoint
- [ ] Read project structure (`controller`, `service`, `repository`)
- Deliverable: Spring Boot app starts locally
- Reflection:
  - Wins:
  - Gaps:

## Day 9 - REST API design (Wed Mar 18)
- [ ] Build CRUD endpoints with `@RestController`
- [ ] Request/response DTOs
- [ ] Correct HTTP status codes
- [ ] Path variables, query params, request body
- [ ] Practice with Postman or curl
- Deliverable: CRUD endpoints running
- Reflection:
  - Wins:
  - Gaps:

## Day 10 - Validation + error handling (Thu Mar 19)
- [ ] Bean validation with `@Valid`
- [ ] Field annotations (`@NotNull`, `@Size`, `@Email`, etc.)
- [ ] Global exception handling with `@ControllerAdvice`
- [ ] Standard API error response model
- [ ] Add pagination and filtering to list endpoint
- Deliverable: validation and consistent errors in API
- Reflection:
  - Wins:
  - Gaps:

## Day 11 - JPA + transactions + performance (Fri Mar 20)
- [ ] Entity mapping and relationships
- [ ] `JpaRepository` usage
- [ ] `@Transactional` behavior
- [ ] Lazy vs eager loading
- [ ] N+1 query awareness and mitigation
- Deliverable: persisted entities with relations
- Reflection:
  - Wins:
  - Gaps:

## Day 12 - Security + JWT (Sat Mar 21)
- [ ] Spring Security basics (filter chain idea)
- [ ] Authentication and authorization concepts
- [ ] JWT flow for stateless APIs
- [ ] Role-based access (`USER`, `ADMIN`)
- [ ] Secure selected endpoints
- Deliverable: protected endpoints with JWT
- Reflection:
  - Wins:
  - Gaps:

## Day 13 - Testing + Testcontainers (Sun Mar 22)
- [ ] Unit tests with JUnit 5 + Mockito
- [ ] Web layer tests (`@WebMvcTest`)
- [ ] Integration tests (`@SpringBootTest`)
- [ ] Testcontainers with PostgreSQL
- [ ] Cover happy path and failure path
- Deliverable: test suite runs green
- Reflection:
  - Wins:
  - Gaps:

## Day 14 - Mock interview + final polish (Mon Mar 23)
- [ ] Explain your project in 2-3 minutes
- [ ] Practice 20 interview questions aloud
- [ ] Review weak answers and fix them
- [ ] Clean README with architecture + run steps
- [ ] Final pass on code quality and API behavior
- Deliverable: interview-ready project + notes
- Reflection:
  - Wins:
  - Gaps:

---

## Target Project Checklist (Task Management API)

- [ ] User registration/login
- [ ] JWT-based auth
- [ ] Role-based authorization
- [ ] Task CRUD
- [ ] Pagination and filtering
- [ ] Input validation
- [ ] Global exception handling
- [ ] PostgreSQL integration
- [ ] DB migration tool (Flyway or Liquibase)
- [ ] Unit tests
- [ ] Integration tests
- [ ] README with run instructions

---

## Interview Questions Checklist

## Core Java
- [ ] Difference between JDK, JRE, JVM
- [ ] Overloading vs overriding
- [ ] Interface vs abstract class
- [ ] `==` vs `.equals()`
- [ ] Why `hashCode()` matters in maps/sets
- [ ] Checked vs unchecked exceptions
- [ ] `final`, `static`, and immutability
- [ ] Stream API benefits and pitfalls

## Spring Boot
- [ ] What problem does Spring Boot solve
- [ ] Dependency Injection in simple words
- [ ] `@Controller` vs `@RestController`
- [ ] Role of `@Service` and `@Repository`
- [ ] How auto-configuration works (high level)
- [ ] Why DTOs are used instead of entities in API

## JPA + Database
- [ ] Entity lifecycle basics
- [ ] Lazy vs eager loading
- [ ] What `@Transactional` does
- [ ] N+1 query problem
- [ ] Index basics and tradeoffs
- [ ] ACID and isolation level concepts

## API + Testing
- [ ] Idempotency (`PUT` vs `POST`)
- [ ] Common status codes (200, 201, 204, 400, 401, 403, 404, 409, 500)
- [ ] Unit test vs integration test
- [ ] Why MockMvc is useful
- [ ] Why Testcontainers helps reliability

---

## Weekly Gate Checks

## End of Week 1 (after Day 7)
- [ ] I can solve basic-to-intermediate Java problems without heavy lookup
- [ ] I can explain OOP and collections clearly
- [ ] I can write clean methods and handle exceptions correctly

## End of Week 2 (after Day 14)
- [ ] I built and tested a complete Spring Boot backend
- [ ] I can explain request flow: controller -> service -> repository -> DB
- [ ] I can defend design choices in interview format

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
