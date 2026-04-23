# Phase 4 — Security + JWT Auth (Mentoring Guide)

> **Your decisions**: Login with email OR username. Register auto-returns token. Roles from day one. 24h token
> expiration.
> **Approach**: I tell you *what* to build and *why*. You figure out *how*. Hints provided when the Spring way is
> non-obvious.

---

## Step 1: Add Dependencies

**What to do**: Add 4 dependencies to your `pom.xml`:

- `spring-boot-starter-security`
- `jjwt-api` (version `0.12.6`)
- `jjwt-impl` (runtime scope)
- `jjwt-jackson` (runtime scope)

**Why**: Spring Security gives you the filter chain, `SecurityFilterChain`, `UserDetails`, `BCryptPasswordEncoder`. JJWT
is the library that creates and parses JWT tokens.

**Verify**: Run `mvn compile`. It should compile. **But** — try hitting `GET /api/tasks` now. What happens?

> [!TIP]
> Spring Security locks down **everything** by default the moment you add the dependency. You should get a login page or
> a 401. This is normal — you'll fix it in Step 8.

**Deliverable**: App compiles, and you've seen Spring Security's default behavior ✅

---

## Step 2: Create the `Role` Enum

**What to do**: Create `Role.java` in the `user/` package. Two values: `USER`, `ADMIN`.

**Why**: You'll store this on the `User` entity. Spring Security uses "authorities" (roles) to decide who can access
what.

**This is identical to your `TaskStatus` enum.** You already know how.

**Deliverable**: `Role.java` exists ✅

---

## Step 3: Write Flyway Migration `V6`

**What to do**: Create `V6__add_role_to_users.sql` in your migration's folder. It should:

- Add a `role` column to the `users` table (VARCHAR, NOT NULL)
- Default existing users to `'USER'`

**Hint**: You've written 5 migrations already. Same pattern.

**Verify**: Run the app. Check your DB — the `role` column should exist on the `users` table with all existing rows set
to `'USER'`.

**Deliverable**: Migration runs cleanly ✅

---

## Step 4: Modify `User` — Implement `UserDetails`

This is the most conceptually new step. Take your time here.

**What to do**:

1. Add a `role` field (type `Role`, use `@Enumerated(EnumType.STRING)`)
2. Make `User` implement `UserDetails`
3. Implement the required methods

**What `UserDetails` requires** (the interface you need to satisfy):

| Method                      | What to return                                                                         | Why                                                        |
|-----------------------------|----------------------------------------------------------------------------------------|------------------------------------------------------------|
| `getAuthorities()`          | A list containing `new SimpleGrantedAuthority("ROLE_" + role.name())`                  | Spring checks this for `@PreAuthorize("hasRole('ADMIN')")` |
| `getUsername()`             | Your `email` field (or `username` — your call, but pick one for Spring's internal use) | Spring uses this to identify the user                      |
| `getPassword()`             | Your `password` field                                                                  | Spring uses this to verify credentials                     |
| `isAccountNonExpired()`     | `true`                                                                                 | You're not implementing account expiry                     |
| `isAccountNonLocked()`      | `true`                                                                                 | You're not implementing account locking                    |
| `isCredentialsNonExpired()` | `true`                                                                                 | You're not implementing credential expiry                  |
| `isEnabled()`               | `true`                                                                                 | You're not implementing disable/enable                     |

> [!IMPORTANT]
> **Conflict alert**: Lombok's `@Getter` will generate `getUsername()` and `getPassword()` from your fields. But
`UserDetails` also requires those methods. This can cause a clash if your field names don't match what Spring expects.
> Think about how Lombok's generated getters and the interface methods interact. If `getUsername()` should return `email`
> for Spring Security purposes, you may need to `@Override` it explicitly.

**Django parallel**: This is like making your model extend `AbstractBaseUser` and defining `USERNAME_FIELD` and
`REQUIRED_FIELDS`.

**What to Google**: `"Spring Security UserDetails implementation example"`

**Verify**: App should still compile. Nothing changes behavior yet.

**Deliverable**: `User` implements `UserDetails`, has a `role` field ✅

---

## Step 5: Update `UserRepository`

**What to do**: Add a method to find a user by email: `Optional<User> findByEmail(String email)`

**Also consider**: Since you want login by email OR username, you might want `findByUsername()` too. Or a custom
`@Query` that checks both. Up to you — start simple with `findByEmail`, add username later.

**You already know how to do this.** You wrote `existsUserByEmail()` — same pattern.

**Deliverable**: Repository can look up users by email ✅

---

## Step 6: Build `JwtService`

**Where**: Create a new `jwt/` package under `taskapi/`. Create `JwtService.java` there.

**What it needs** (3 methods):

| Method              | Input                           | Output               | What it does                                                                                                       |
|---------------------|---------------------------------|----------------------|--------------------------------------------------------------------------------------------------------------------|
| `generateToken()`   | `UserDetails`                   | `String` (the token) | Creates a JWT with the username as subject, current time as issued-at, +24h as expiry, signed with your secret key |
| `extractUsername()` | `String` (token)                | `String` (username)  | Parses the token and pulls out the `subject` claim                                                                 |
| `isTokenValid()`    | `String` (token), `UserDetails` | `boolean`            | Checks: does the username match? Is the token not expired?                                                         |

**Config you'll need in `application.yml`**:

```yaml
jwt:
  secret: <a-base64-encoded-string-at-least-256-bits-long>
  expiration: 86400000   # 24 hours in milliseconds
```

**How to read config values in Spring**: Use `@Value("${jwt.secret}")` on a field. You haven't done this before — worth
Googling.

**What to Google**:

- `"jjwt 0.12 generate token example"` (the API changed significantly in 0.12 vs older tutorials)
- `"Spring @Value annotation"`

> [!WARNING]
> Most tutorials you'll find use JJWT 0.11 or older. The 0.12 API is different — `Jwts.builder()` and `Jwts.parser()`
> have a new fluent syntax. Make sure you're looking at 0.12 examples.
> The [JJWT GitHub README](https://github.com/jwtk/jjwt) is the best reference.

**Key concept**: The secret key must be a `SecretKey` object, not a raw string. You'll need to decode your base64 secret
and build a key with `Keys.hmacShaKeyFor(bytes)`.

**Verify**: Write a quick test mentally — if you call `generateToken(user)` then `extractUsername(token)`, you should
get the same username back.

**Deliverable**: `JwtService` can generate, parse, and validate tokens ✅

---

## Step 7: Build `JwtAuthenticationFilter`

**Where**: Same `jwt/` package. Create `JwtAuthenticationFilter.java`.

**What it is**: A class that extends `OncePerRequestFilter` — Spring runs it once per HTTP request.

**What to implement**: Override `doFilterInternal(request, response, filterChain)`

**The logic you need to write** (in this exact order):

```
1. Get the "Authorization" header from the request
2. If header is null OR doesn't start with "Bearer " → call filterChain.doFilter() and return
3. Extract the token (everything after "Bearer ")
4. Extract username from token using JwtService
5. If username != null AND SecurityContextHolder has NO existing authentication:
   a. Load UserDetails from the database (by username/email)
   b. If token is valid (using JwtService):
      - Create a UsernamePasswordAuthenticationToken
      - Set it in the SecurityContextHolder
6. Call filterChain.doFilter() (always — let the chain continue)
```

**Django parallel**: This is like writing a custom authentication backend in `AUTHENTICATION_BACKENDS`, but it runs as
middleware.

**What to Google**: `"Spring Security OncePerRequestFilter JWT example"`

**Key classes you'll use**:

- `HttpServletRequest` → `request.getHeader("Authorization")`
- `SecurityContextHolder.getContext().setAuthentication(...)`
- `UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())`

> [!TIP]
> The filter doesn't reject requests itself. If the token is missing/invalid, it simply doesn't set the SecurityContext.
> The `SecurityFilterChain` (Step 8) is what actually returns 401 when it sees no authentication.

**Deliverable**: Filter class compiles ✅

---

## Step 8: Build `SecurityConfig`

**Where**: Modify your existing `config/SecurityConfig.java` (or rename `WebConfig.java`).

**What to do**: Create a `@Configuration` class with these `@Bean` methods:

| Bean                    | What it does                                                                                          |
|-------------------------|-------------------------------------------------------------------------------------------------------|
| `SecurityFilterChain`   | Defines public vs protected routes, disables CSRF, sets stateless sessions, registers your JWT filter |
| `PasswordEncoder`       | Returns `new BCryptPasswordEncoder()` — used everywhere passwords are hashed/verified                 |
| `AuthenticationManager` | Needed by login logic to verify credentials                                                           |

**Route rules to configure**:

| Pattern                | Access             |
|------------------------|--------------------|
| `/api/auth/**`         | `permitAll()`      |
| `/api/health`          | `permitAll()`      |
| `/swagger-ui/**`       | `permitAll()`      |
| `/v3/api-docs/**`      | `permitAll()`      |
| `DELETE /api/tasks/**` | `hasRole("ADMIN")` |
| `DELETE /api/users/**` | `hasRole("ADMIN")` |
| Everything else        | `authenticated()`  |

**What to Google**: `"Spring Security 6 SecurityFilterChain JWT configuration"`

> [!WARNING]
> Spring Security 6 (which you have with Spring Boot 3.2) uses the lambda DSL: `http.csrf(csrf -> csrf.disable())` — NOT
> the old `.csrf().disable()` chain. Make sure you're looking at Spring Security 6 examples.

**Verify**: After this step, your app should start again. `/api/health` should work without a token. `/api/tasks` should
return 401.

**Deliverable**: Public routes accessible, protected routes return 401 ✅

---

## Step 9: Build Auth DTOs

**Where**: Create `auth/dto/` package.

**3 classes**:

| Class             | Fields                                       | Validation                                                                    |
|-------------------|----------------------------------------------|-------------------------------------------------------------------------------|
| `RegisterRequest` | `username`, `email`, `password`              | Same rules as your `UserRequest` — `@NotBlank`, `@Email`, `@Size`, `@Pattern` |
| `LoginRequest`    | `identifier` (email or username), `password` | `@NotBlank` on both                                                           |
| `AuthResponse`    | `token`, `username`, `role`                  | No validation — this is what you return                                       |

**Design choice for login**: Since you want login by email OR username, use a single `identifier` field. In your
service, try email first, then username.

**Deliverable**: DTOs compile ✅

---

## Step 10: Build `AuthController` + Auth Logic

**Where**: Create `auth/AuthController.java`.

**Two endpoints**:

### `POST /api/auth/register`

```
Accept RegisterRequest
→ Check email/username uniqueness (you already have this logic in UserService)
→ Hash password with BCryptPasswordEncoder.encode()
→ Set role to Role.USER
→ Save user
→ Generate JWT with JwtService
→ Return AuthResponse with token
```

### `POST /api/auth/login`

```
Accept LoginRequest
→ Find user by identifier (try email, if not found try username)
→ Verify password with BCryptPasswordEncoder.matches(rawPassword, hashedPassword)
→ If no match → throw 401 exception
→ Generate JWT
→ Return AuthResponse with token
```

**Decision**: You can put the auth logic directly in the controller, or create an `AuthService`. I'd recommend an
`AuthService` — keeps consistent with your pattern.

> [!TIP]
> For the login, you can also use Spring's `AuthenticationManager.authenticate()` which does the password check for you.
> Google `"Spring AuthenticationManager authenticate example"`. Both approaches work — the manual way (
`passwordEncoder.matches()`) is easier to understand.

**Verify**: Test with Postman:

1. `POST /api/auth/register` with a new user → should get a token back
2. `POST /api/auth/login` with the same credentials → should get a token back
3. Copy the token

**Deliverable**: Can register and login, tokens are returned ✅

---

## Step 11: Modify `UserService.createUser()`

**What to do**: Your existing `createUser()` saves passwords in plain text. Now hash them:

```
Before: userRepository.save(user)
After:  user.setPassword(passwordEncoder.encode(user.getPassword()))
        user.setRole(Role.USER)  // default role
        userRepository.save(user)
```

**Inject** `PasswordEncoder` into `UserService` via constructor injection — same pattern you already use.

**Deliverable**: All new users get hashed passwords ✅

---

## Step 12: Add Auth Exception Handling

**Where**: Your existing `GlobalExceptionHandler.java`

**What to handle**:

- Create `UnauthorizedException` (extends `RuntimeException`) → return 401
- Optionally handle Spring Security's `AuthenticationException` → return 401
- Optionally handle `AccessDeniedException` → return 403

> [!TIP]
> Spring Security handles 401/403 before your controllers run (it's a filter). So `@ControllerAdvice` might not catch
> these. You may need to create a custom `AuthenticationEntryPoint` (for 401) and `AccessDeniedHandler` (for 403) and
> register them in your `SecurityFilterChain`. Google `"Spring Security custom AuthenticationEntryPoint JWT"` if your 401s
> return HTML instead of your JSON format.

**Deliverable**: Auth failures return your `ApiResponse` JSON format, not Spring's default HTML ✅

---

## Full Test Flow (Step 13 — Prove It Works)

Run through this exact sequence in Postman:

| #  | Request                                                                                           | Expected                        |
|----|---------------------------------------------------------------------------------------------------|---------------------------------|
| 1  | `GET /api/health`                                                                                 | 200 ✅ (public)                  |
| 2  | `GET /api/tasks` (no token)                                                                       | 401 ❌                           |
| 3  | `POST /api/auth/register` `{"username":"testuser","email":"test@test.com","password":"Test1234"}` | 201 + token                     |
| 4  | `POST /api/auth/login` `{"identifier":"test@test.com","password":"Test1234"}`                     | 200 + token                     |
| 5  | `POST /api/auth/login` `{"identifier":"testuser","password":"Test1234"}`                          | 200 + token (login by username) |
| 6  | `GET /api/tasks` with `Authorization: Bearer <token>`                                             | 200 ✅                           |
| 7  | `POST /api/tasks` with token                                                                      | 201 ✅                           |
| 8  | `DELETE /api/tasks/1` with USER role token                                                        | 403 ❌ (not admin)               |
| 9  | Manually set a user's role to ADMIN in DB, login again                                            |                                 |
| 10 | `DELETE /api/tasks/1` with ADMIN token                                                            | 204 ✅                           |
| 11 | `POST /api/auth/login` with wrong password                                                        | 401 ❌                           |
| 12 | `GET /api/tasks` with expired/garbage token                                                       | 401 ❌                           |

---

## Concepts You'll Learn Along the Way

| When    | Concept                        | Quick Hint                                                                        |
|---------|--------------------------------|-----------------------------------------------------------------------------------|
| Step 4  | Java interfaces (`implements`) | Like Python's `ABC`. You must implement ALL methods.                              |
| Step 6  | `@Value` config injection      | Spring reads from `application.yml` and injects into fields                       |
| Step 6  | Builder pattern                | `Jwts.builder().subject(...).issuedAt(...).build()` — method chaining             |
| Step 7  | Filter chain pattern           | Each filter does its job, then calls `filterChain.doFilter()` to pass to the next |
| Step 8  | Lambda DSL                     | `http.csrf(csrf -> csrf.disable())` — lambdas configuring a builder               |
| Step 8  | Bean registration              | `@Bean` methods in `@Configuration` classes = Spring manages the returned object  |
| Step 10 | `BCryptPasswordEncoder`        | One-way hash. `encode()` to hash, `matches()` to verify. Never decrypt.           |

---

## Common Pitfalls (Save Yourself Hours)

1. **"403 on everything even with a valid token"** → Your filter isn't setting the SecurityContext. Debug by adding
   `log.debug()` inside the filter to trace each step.

2. **"My `/swagger-ui` is blocked"** → The actual Swagger path might be `/swagger-ui/index.html` or `/swagger-ui.html`.
   Add both patterns to permitAll.

3. **"BCrypt error: Encoded password does not look like BCrypt"** → You have old plain-text passwords in the DB. Either
   re-register those users or manually hash their passwords.

4. **"401 returns HTML, not JSON"** → Spring Security's default error handling returns HTML. You need a custom
   `AuthenticationEntryPoint` (see Step 12).

5. **"Lombok and UserDetails clash"** → If `getUsername()` from Lombok conflicts with `getUsername()` from
   `UserDetails`, explicitly `@Override` the method.

---

## Suggested Pace

| Day   | Steps                                                          | Hours |
|-------|----------------------------------------------------------------|-------|
| Day 1 | Steps 1–5 (deps, role, migration, User entity, repo)           | ~3h   |
| Day 2 | Steps 6–7 (JwtService + filter)                                | ~3h   |
| Day 3 | Steps 8–10 (security config, DTOs, auth controller)            | ~3h   |
| Day 4 | Steps 11–13 (hash passwords, exception handling, full testing) | ~2h   |

Good luck — you've got this. The hardest part is Step 6–7 (JWT + Filter). Everything else maps to patterns you've
already used. 🚀
