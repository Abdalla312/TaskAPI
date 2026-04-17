## Plan: Handle User Not-Found Flow

Fix the `Optional<User>` to `UserResponseDTO` conversion path so `getUserById` compiles and behaves predictably. The core idea is to map the repository `Optional` in service logic, then choose one consistent not-found strategy (controller-level 404 or exception-driven 404). While touching this flow, align the update endpoint types to avoid current service/controller signature mismatch.

### Steps
1. Review `User` read/update flow in [UserService.java](E:/Dev/java/taskapi/src/main/java/com/example/taskapi/service/UserService.java) and [UserController.java](E:/Dev/java/taskapi/src/main/java/com/example/taskapi/controller/UserController.java) for consistency.
2. Refactor `UserService.getUserById` to map `Optional<User>` via `Optional.map(userMapper::toDTO)` using `UserMapper.toDTO(User)` in [UserMapper.java](E:/Dev/java/taskapi/src/main/java/com/example/taskapi/mapper/UserMapper.java).
3. Keep mapper scope entity/list-focused; do not add `toDTO(Optional<User>)` overload in [UserMapper.java](E:/Dev/java/taskapi/src/main/java/com/example/taskapi/mapper/UserMapper.java).
4. Standardize 404 behavior for `getUserById`: either `ResponseEntity.notFound()` in [UserController.java](E:/Dev/java/taskapi/src/main/java/com/example/taskapi/controller/UserController.java) or `ResourceNotFoundException` with [GlobalExceptionHandler.java](E:/Dev/java/taskapi/src/main/java/com/example/taskapi/exception/GlobalExceptionHandler.java).
5. Align update method contracts (`UserRequestDTO` vs `User`) between [UserController.java](E:/Dev/java/taskapi/src/main/java/com/example/taskapi/controller/UserController.java) and [UserService.java](E:/Dev/java/taskapi/src/main/java/com/example/taskapi/service/UserService.java) to remove current compile error.

### Further Considerations
1. Which API style do you want? Option A: controller returns 404 directly; Option B: service throws `ResourceNotFoundException`; Option C: mixed style (less consistent).
2. For update semantics, should `PATCH` do partial field merge or full replacement with `UserRequestDTO`?
3. Draft ready for your review: confirm the not-found strategy first, then finalize update contract choices.
