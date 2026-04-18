package com.example.taskapi.task;

import com.example.taskapi.task.dto.TaskRequest;
import com.example.taskapi.task.dto.TaskResponse;
import com.example.taskapi.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    // Constructor injection — Spring sees TaskService is needed and provides it.
    // Django equivalent: there isn't one. Django uses imports instead of DI.
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping(params = "status")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@RequestParam String status) {
        return ResponseEntity.ok(taskService.findTasksByStatus(status));
    }

    @GetMapping(params = "title")
    public ResponseEntity<List<TaskResponse>> getTaskByTitle(@RequestParam String title) {
        return ResponseEntity.ok(taskService.findTaskByTitle(title));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));        // not found → 404
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest task) {
        TaskResponse created = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);  // 201
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskRequest task) {
        return ResponseEntity.ok(taskService.updateTask(id, task));        // not found → 404
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.noContent().build();             // deleted → 204
        }
        return ResponseEntity.notFound().build();                  // not found → 404
    }

    @PatchMapping("/{id}/assign/{userId}")
    public ResponseEntity<TaskResponse> assignTaskToUser(@PathVariable Long id, @PathVariable Long userId) {
        return taskService.assignTaskToUser(id, userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User or Task id not found."));
    }


}
