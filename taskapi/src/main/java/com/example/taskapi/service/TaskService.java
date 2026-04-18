package com.example.taskapi.service;

import com.example.taskapi.dto.TaskRequest;
import com.example.taskapi.dto.TaskResponse;
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


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    // constructor
    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponse createTask(TaskRequest task) {
        Task taskEntity = taskMapper.toEntity(task);
        Long userId = taskEntity.getUser() != null ? taskEntity.getUser().getId() : null;
        if (userId == null) {
            // Allow unassigned task creation; assignment can happen later.
            taskEntity.setUser(null);
            return taskMapper.toDTO(taskRepository.save(taskEntity));
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        taskEntity.setUser(user);
        return taskMapper.toDTO(taskRepository.save(taskEntity));
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream().map(taskMapper::toDTO)
                .toList();
    }

    public TaskResponse getTaskById(Long id) {

        return taskRepository.findById(id).map(taskMapper::toDTO).orElseThrow(() -> new ResourceNotFoundException("task id not found"));
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest updatedTask) {
        //find the task id first
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task Id not found"));
        taskMapper.updateEntityFromDto(updatedTask, task);
        return taskMapper.toDTO(task);
    }


    public boolean deleteTask(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task Id not found."));
        taskRepository.deleteById(id);
        return true;
    }

    @Transactional
    public Optional<TaskResponse> assignTaskToUser(Long id, Long userId) {
        Task assignedTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        User assignedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        assignedTask.setUser(assignedUser);
        return Optional.of(taskRepository.save(assignedTask)).map(taskMapper::toDTO);
    }

    //find task by status function using query method
    public List<TaskResponse> findTasksByStatus(String status) {
        return taskRepository.findAllByStatus(TaskStatus.valueOf(status))
                .stream().map(taskMapper::toDTO).toList();
    }

    public List<TaskResponse> findTaskByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title)
                .stream().map(taskMapper::toDTO).toList();
    }

}
