package com.example.taskapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Task entity — the core domain object of your API.
 *
 * Django equivalent:
 *   class Task(models.Model):
 *       title = models.CharField(max_length=200)
 *       description = models.TextField(blank=True)
 *       status = models.CharField(choices=TaskStatus.choices)
 *       created_at = models.DateTimeField(auto_now_add=True)
 *
 * For now, this is a plain Java class (POJO).
 * In Phase 2 (Day 4), you'll add @Entity to make it a JPA entity.
 *
 * TODO (Day 2): Add fields — title, description, status, createdAt
 * TODO (Day 4): Add @Entity, @Id, @GeneratedValue annotations
 * TODO (Day 5): Add @ManyToOne relationship to User
 */
@Entity
@Table(name="tasks")
public class Task {

    @Id @GeneratedValue private Long id;

    private String title;
    private String description;
    private String status; // TODO (Day 5): Replace with TaskStatus enum

    // --- Constructors ---

    public Task() {}

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = "TODO";
    }

    // --- Getters and Setters ---
    // Django does this automatically. In Java, you write them explicitly.
    // When you add Lombok later, @Data generates all of these for you.

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', status='" + status + "'}";
    }
}
