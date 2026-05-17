package com.example.taskapi.user;

import com.example.taskapi.common.apiResponse.ApiResponse;
import com.example.taskapi.common.pagination.PageResponse;
import com.example.taskapi.user.dto.UserRequest;
import com.example.taskapi.user.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Show authenticated user details")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        return ResponseEntity.ok()
                .body(ApiResponse.ok(userService.getUserDetails(), "Success"));
    }

    @Operation(summary = "Edit/Update authenticated user details")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "data validation failed"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@RequestBody UserRequest userDTO) {
        return ResponseEntity.ok(
                ApiResponse.ok(userService.updateUser(userDTO)
                        , "success"));
    }

    @Operation(summary = "Admin show all user details")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(userService.getAllUsers(pageable)
                        , "success"));
    }

    @Operation(summary = "Admin show certain user")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(userService.getUserById(id)
                        , "success"));
    }

    @Operation(summary = "Admin delete certain user")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // deleted -> 204
    }


    @Operation(summary = "Admin make user ROLE_ADMIN")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "forbidden"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> makeAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.setAdmin(id), "success"));
    }
}
