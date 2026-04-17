package com.example.taskapi.service;

import com.example.taskapi.dto.TaskRequestDTO;
import com.example.taskapi.dto.TaskResponseDTO;
import com.example.taskapi.exception.ResourceNotFoundException;
import com.example.taskapi.mapper.TaskMapper;
import com.example.taskapi.model.Task;
import com.example.taskapi.model.TaskStatus;
import com.example.taskapi.model.User;
import com.example.taskapi.repository.TaskRepository;
import com.example.taskapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TaskService {

    // In-memory storage for Phase 1. Replaced with PostgreSQL in Phase 2.
    // Think of this like a temporary "database" — similar to Django's
    // in-memory SQLite for tests.

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    @Autowired
    private final TaskMapper taskMapper;

    // constructor
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponseDTO createTask(TaskRequestDTO task) {
        Task taskEntity = taskMapper.toEntity(task);
        Long userId = taskEntity.getUser() != null ? taskEntity.getUser().getId() : null;
        if (userId == null) {
            // Allow unassigned task creation; assignment can happen later.
            taskEntity.setUser(null);
            return taskMapper.toDTO(taskRepository.save(taskEntity));
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        taskEntity.setUser(user);
        return taskMapper.toDTO(taskRepository.save(taskEntity));
    }

    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream().map(taskMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<TaskResponseDTO> getTaskById(Long id) {

        return taskRepository.findById(id).map(taskMapper::toDTO);
    }

    public Optional<TaskResponseDTO> updateTask(Long id, TaskRequestDTO updatedTask) {
        //find the task id first
        Task foundTask = taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found"));
        // then convert the dtoRequest into entity shape
        Task updatedTaskEntity = taskMapper.toEntity(updatedTask);
        // then update entity
        // then update
        // convert into dtoResponse shape
        // then return it
        return Optional.of(taskRepository.save(updatedTaskEntity)).map(taskMapper::toDTO);
    }


    public boolean deleteTask(Long id) {
        try {
            taskRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public Optional<TaskResponseDTO> assignTaskToUser(Long id, Long userId) {
        Task assignedTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        User assignedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        assignedTask.setUser(assignedUser);

        return Optional.of(taskRepository.save(assignedTask)).map(taskMapper::toDTO);
    }

    //find task by status function using query method
    //@Query("select * from tasks where taskstatus == status")
    public List<TaskResponseDTO> findTasksByStatus(TaskStatus status) {
        return taskRepository.findAllByStatus(status).stream().map(taskMapper::toDTO).toList();
    }

    public List<TaskResponseDTO> findTaskByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title)
                .stream().map(taskMapper::toDTO).toList();
    }

}
