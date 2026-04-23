package com.example.taskapi.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByStatus(TaskStatus status, Pageable pageable);
    //findByTitleContainingIgnoreCase
    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
