package com.example.taskapi.task;

import com.example.taskapi.common.exception.BadRequestException;
import com.example.taskapi.common.exception.ResourceNotFoundException;
import com.example.taskapi.common.pagination.PageResponse;
import com.example.taskapi.task.dto.TaskRequest;
import com.example.taskapi.task.dto.TaskResponse;
import com.example.taskapi.user.User;
import com.example.taskapi.user.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponse createTask(TaskRequest task) {
        log.info("Create task requested: userId={}, title={}, status={}",
                task.getUserId(), task.getTitle(), task.getTaskStatus());
        Task taskEntity = taskMapper.toEntity(task);
        Long userId = taskEntity.getUser() != null ? taskEntity.getUser().getId() : null;
        if (userId == null) {
            // Allow unassigned task creation; assignment can happen later.
            taskEntity.setUser(null);
            Task saved = taskRepository.save(taskEntity);
            log.info("Task created (unassigned): taskId={}", saved.getId());
            return taskMapper.toDTO(saved);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        taskEntity.setUser(user);
        Task saved = taskRepository.save(taskEntity);
        log.info("Task created: taskId={}, userId={}", saved.getId(), userId);
        return taskMapper.toDTO(saved);
    }

    public PageResponse<TaskResponse> getAllTasks(Pageable pageable) {
        log.debug("Fetching all tasks requested.");
        return PageResponse.from(
                taskRepository.findAll(pageable)
                        .map(taskMapper::toDTO));
    }

    public TaskResponse getTaskById(Long id) {
        log.debug("Fetch task with id={}", id);
        return taskRepository.findById(id)
                .map(taskMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("task id not found"));
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest updatedTask) {
        // find the task first, then update in-place (keeps the original ID for JPA update semantics)
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task Id not found"));

        log.debug("task update from {} to {}", task, taskMapper.toEntity(updatedTask));
        taskMapper.updateEntityFromDto(updatedTask, task);

        if (updatedTask.getUserId() != null) {
            User user = userRepository.findById(updatedTask.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + updatedTask.getUserId()));
            task.setUser(user);
        }
        taskRepository.save(task);
        return taskMapper.toDTO(task);
    }


    public void deleteTask(Long id) {

        taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task Id not found."));
        taskRepository.deleteById(id);
        log.debug("task delete id={}", id);
    }

    @Transactional
    public TaskResponse assignTaskToUser(Long id, Long userId) {
        Task assignedTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        User assignedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        assignedTask.setUser(assignedUser);
        log.debug("assign task id={} to userId={}", id, userId);
        return taskMapper.toDTO(taskRepository.save(assignedTask));
    }

    //find task by status function using query method
    public Page<TaskResponse> findTasksByStatus(String status, Pageable pageable) {
        status = status.toUpperCase();
        try {
            TaskStatus.valueOf(status);
            return taskRepository.findAllByStatus(TaskStatus.valueOf(status), pageable)
                    .map(taskMapper::toDTO);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid input { TaskStatus }");
        }
    }

    public Page<TaskResponse> findTaskByTitle(String title, Pageable pageable) {

        return taskRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(taskMapper::toDTO);
    }

}
