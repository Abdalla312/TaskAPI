package com.example.taskapi.service;

import com.example.taskapi.model.Task;
import com.example.taskapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Task service — business logic lives here.
 *
 * Django equivalent: this is like a service layer you might extract from
 * your views.py, or helper functions in utils.py.
 *
 * Key rule: Controllers handle HTTP. Services handle logic. Always.
 *
 * @Service tells Spring: "create one instance of this class and manage it"
 * (this is Dependency Injection — Spring creates it, not you)
 *
 * TODO (Day 2): Implement createTask() and getAllTasks()
 * TODO (Day 3): Add getTaskById(), updateTask(), deleteTask()
 * TODO (Day 4): Replace HashMap with TaskRepository (JPA)
 */
@Service
public class TaskService {

    // In-memory storage for Phase 1. Replaced with PostgreSQL in Phase 2.
    // Think of this like a temporary "database" — similar to Django's
    // in-memory SQLite for tests.
    @Autowired private final TaskRepository taskRepository;
    private Long nextId = 1L;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    /**
     * Create a new task.
     * Django equivalent: Task.objects.create(title=title, description=description)
     */
    public Task createTask(Task task) {

        return taskRepository.save(task);
    }

    /**
     * Get all tasks.
     * Django equivalent: Task.objects.all()
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    /**
     * Get a task by ID.
     * Django equivalent: Task.objects.filter(pk=id).first()
     *
     * Optional<Task> means: "this might return a Task, or it might be empty"
     * It's Java's way of avoiding null — like returning None in Python
     * but forcing you to handle the "not found" case explicitly.
     */

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Update an existing task.
     * Django equivalent: task = Task.objects.get(pk=id); task.title = new_title; task.save()
     */
    public Optional<Task> updateTask(Long id, Task updatedTask) {

        return Optional.of(taskRepository.save(updatedTask));
    }

    /**
     * Delete a task.
     * Django equivalent: Task.objects.filter(pk=id).delete()
     */
    public void deleteTask(Long id) {

        taskRepository.deleteById(id);
    }
}
