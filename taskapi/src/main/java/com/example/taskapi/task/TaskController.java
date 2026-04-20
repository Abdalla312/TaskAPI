package com.example.taskapi.task;

import com.example.taskapi.common.apiResponse.ApiResponse;
import com.example.taskapi.common.pagination.PageResponse;
import com.example.taskapi.common.validation.OnPatch;
import com.example.taskapi.task.dto.TaskRequest;
import com.example.taskapi.task.dto.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getAllTasks(Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        taskService.getAllTasks(pageable),
                        "Success"));
    }

    @GetMapping(params = "status")
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getTasksByStatus(@RequestParam String status, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        PageResponse.from(
                                taskService.findTasksByStatus(status, pageable)),
                        "success"));
    }

    @GetMapping(params = "title")
    public ResponseEntity<ApiResponse<PageResponse<TaskResponse>>> getTaskByTitle(@RequestParam String title, Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        PageResponse.from(
                                taskService.findTaskByTitle(title, pageable)),
                        "success"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(taskService.getTaskById(id), "success"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@RequestBody @Validated(OnPatch.class) TaskRequest task) {
        TaskResponse created = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.created("success", created));  // 201
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(@PathVariable Long id, @RequestBody @Valid TaskRequest task) {
        return ResponseEntity.ok(ApiResponse.ok(taskService.updateTask(id, task), "success"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();             // deleted -> 204
    }

    @PatchMapping("/{id}/assign/{userId}")
    public ResponseEntity<ApiResponse<TaskResponse>> assignTaskToUser(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok(taskService.assignTaskToUser(id, userId), "success"));
    }


}
