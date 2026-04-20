package com.example.taskapi.task;

import com.example.taskapi.common.exception.ResourceNotFoundException;
import com.example.taskapi.common.pagination.PageResponse;
import com.example.taskapi.task.dto.TaskRequest;
import com.example.taskapi.task.dto.TaskResponse;
import com.example.taskapi.user.User;
import com.example.taskapi.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


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

    public PageResponse<TaskResponse> getAllTasks(Pageable pageable) {
        return PageResponse.from(
                taskRepository.findAll(pageable)
                        .map(taskMapper::toDTO));
    }

    public TaskResponse getTaskById(Long id) {

        return taskRepository.findById(id)
                .map(taskMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("task id not found"));
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest updatedTask) {
        // find the task first, then update in-place (keeps the original ID for JPA update semantics)
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task Id not found"));

        taskMapper.updateEntityFromDto(updatedTask, task);

        if (updatedTask.getUserId() != null) {
            User user = userRepository.findById(updatedTask.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + updatedTask.getUserId()));
            task.setUser(user);
        }

        return taskMapper.toDTO(task);
    }


    public void deleteTask(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task Id not found."));
        taskRepository.deleteById(id);
    }

    @Transactional
    public TaskResponse assignTaskToUser(Long id, Long userId) {
        Task assignedTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        User assignedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        assignedTask.setUser(assignedUser);
        return taskMapper.toDTO(taskRepository.save(assignedTask));
    }

    //find task by status function using query method
    public Page<TaskResponse> findTasksByStatus(String status, Pageable pageable) {
        try {
            TaskStatus.valueOf(status);
            return taskRepository.findAllByStatus(TaskStatus.valueOf(status), pageable)
                    .map(taskMapper::toDTO);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid input { TaskStatus }");
        }
    }

    public Page<TaskResponse> findTaskByTitle(String title, Pageable pageable) {
        return taskRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(taskMapper::toDTO);
    }

}
