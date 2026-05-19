package com.example.taskapi.task;

import com.example.taskapi.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findAllByStatusAndUser(TaskStatus status, User user, Pageable pageable);

    Page<Task> findByTitleContainingIgnoreCaseAndUser(String title, User user, Pageable pageable);

    Page<Task> findTaskByUser(User user, Pageable pageable);

    java.util.Optional<Task> findByIdAndUser(Long id, User user);
}
