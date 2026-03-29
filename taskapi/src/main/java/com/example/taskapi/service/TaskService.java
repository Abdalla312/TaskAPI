package com.example.taskapi.service;

import com.example.taskapi.model.Task;
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
    private final Map<Long, Task> tasks = new HashMap<>();
    private Long nextId = 1L;

    /**
     * Create a new task.
     * Django equivalent: Task.objects.create(title=title, description=description)
     */
    public Task createTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task;
    }

    /**
     * Get all tasks.
     * Django equivalent: Task.objects.all()
     */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
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
        return Optional.ofNullable(tasks.get(id));
    }

    /**
     * Update an existing task.
     * Django equivalent: task = Task.objects.get(pk=id); task.title = new_title; task.save()
     */
    public Optional<Task> updateTask(Long id, Task updatedTask) {
        if (!tasks.containsKey(id)) {
            return Optional.empty();
        }
        updatedTask.setId(id);
        tasks.put(id, updatedTask);
        return Optional.of(updatedTask);
    }

    /**
     * Delete a task.
     * Django equivalent: Task.objects.filter(pk=id).delete()
     */
    public boolean deleteTask(Long id) {
        return tasks.remove(id) != null;
    }
}
