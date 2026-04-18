package com.example.taskapi.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
    List<Task> findAllByStatus(TaskStatus status);
    //findByTitleContainingIgnoreCase
    List<Task> findByTitleContainingIgnoreCase(String title);
}
