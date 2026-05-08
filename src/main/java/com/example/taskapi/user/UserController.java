package com.example.taskapi.user;

import com.example.taskapi.common.apiResponse.ApiResponse;
import com.example.taskapi.common.pagination.PageResponse;
import com.example.taskapi.user.dto.UserRequest;
import com.example.taskapi.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(userService.getAllUsers(pageable)
                        , "success"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(userService.getUserById(id)
                        , "success"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody @Valid UserRequest dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(userService.createUser(dto), "Success"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequest userDTO)
    {
        return ResponseEntity.ok(
                ApiResponse.ok(userService.updateUser(id, userDTO)
                        , "success"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // deleted -> 204
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> makeAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.setAdmin(id), "success"));
    }
}
