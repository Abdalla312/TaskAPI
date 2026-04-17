package com.example.taskapi.controller;

import com.example.taskapi.dto.TaskRequestDTO;
import com.example.taskapi.dto.TaskResponseDTO;
import com.example.taskapi.exception.ResourceNotFoundException;
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.service.TaskService;
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
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping(params = "status")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(@RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.findTasksByStatus(status));
    }

    @GetMapping(params = "title")
    public ResponseEntity<List<TaskResponseDTO>> getTaskByTitle(@RequestParam String title) {
        return ResponseEntity.ok(taskService.findTaskByTitle(title));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)                           // found → 200
                .orElse(ResponseEntity.notFound().build());        // not found → 404
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO task) {
        TaskResponseDTO created = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);  // 201
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO task) {
        return taskService.updateTask(id, task)
                .map(ResponseEntity::ok)                           // updated → 200
                .orElse(ResponseEntity.notFound().build());        // not found → 404
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.noContent().build();             // deleted → 204
        }
        return ResponseEntity.notFound().build();                  // not found → 404
    }

    @PatchMapping("/{id}/assign/{userId}")
    public ResponseEntity<TaskResponseDTO> assignTaskToUser(@PathVariable Long id, @PathVariable Long userId) {
        return taskService.assignTaskToUser(id, userId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User or Task id not found."));
    }


}
