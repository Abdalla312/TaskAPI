package com.example.taskapi.task;

import com.example.taskapi.task.dto.TaskResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * JPA repositories go here.
 * <p>
 * Django equivalent: This replaces Task.objects (the default manager).
 * <p>
 * A repository interface like:
 * public interface TaskRepository extends JpaRepository<Task, Long> {}
 * <p>
 * gives you .save(), .findById(), .findAll(), .deleteById() for free.
 * Like Django's QuerySet but through an interface.
 * <p>
 * TODO (Day 4): Create TaskRepository
 * TODO (Day 5): Create UserRepository
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByStatus(TaskStatus status, Pageable pageable);
    //findByTitleContainingIgnoreCase
    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
