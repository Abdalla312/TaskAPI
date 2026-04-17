package com.example.taskapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name = "tasks", indexes = {
        @Index(name = "idx_task_status", columnList = "status")
})
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //foreign key users.id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(nullable = false)
    private String title;


    @Column(columnDefinition = "TEXT")
    private String description;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status; // TODO (Day 5): Replace with TaskStatus enum

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // --- Constructors ---
    public Task() {
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.TODO;
    }

    // --- Getters and Setters ---
    // Django does this automatically. In Java, you write them explicitly.
    // When you add Lombok later, @Data generates all of these for you.

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', status='" + status + "'}";
    }
}
