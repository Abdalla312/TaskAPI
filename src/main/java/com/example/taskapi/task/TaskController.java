package com.example.taskapi.task;

import com.example.taskapi.common.apiResponse.ApiResponse;
import com.example.taskapi.common.pagination.PageResponse;
import com.example.taskapi.common.validation.OnCreate;
import com.example.taskapi.common.validation.OnPatch;
import com.example.taskapi.task.dto.TaskRequest;
import com.example.taskapi.task.dto.TaskResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "List all tasks")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-all")
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getAllTasks(Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        taskService.getAllTasks(pageable),
                        "success"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getAllTasksByUser(Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        taskService.getAllUserTasks(pageable),
                        "success"
                )
        );
    }

    @Operation(summary = "Get task by status")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @GetMapping(params = "status")
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getTasksByStatus(@RequestParam String status, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        PageResponse.from(
                                taskService.findTasksByStatus(status, pageable)),
                        "success"));
    }

    @Operation(summary = "Get task by title")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @GetMapping(params = "title")
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getTaskByTitle(@RequestParam String title, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        PageResponse.from(
                                taskService.findTaskByTitle(title, pageable)),
                        "success"));
    }

    @Operation(summary = "Get task by id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(taskService.getTaskById(id), "success"));
    }

    @Operation(summary = "Create new task")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "conflict"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@RequestBody @Validated(OnCreate.class) TaskRequest taskRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.created(taskService.createTask(taskRequest),
                        "success"));  // 201
    }

    @Operation(summary = "Patch task by id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(@PathVariable Long id, @RequestBody @Validated(OnPatch.class) TaskRequest task) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        taskService.updateTask(id, task),
                        "success"));
    }

    @Operation(summary = "Delete task by id")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "server error")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();             // deleted -> 204
    }

}
