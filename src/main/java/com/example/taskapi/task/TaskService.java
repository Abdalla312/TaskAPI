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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;


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

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsernameOrEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Authentication"));
    }

    public TaskResponse createTask(TaskRequest task) {
        Task taskEntity = taskMapper.toEntity(task);
        User user = getAuthenticatedUser();
        if (taskEntity.getStatus() == null) {
            taskEntity.setStatus(TaskStatus.TODO);
        }
        taskEntity.setUser(user);
        Task saved = taskRepository.save(taskEntity);
        log.info("Task Created: userId={}, title={}, status={}",
                taskEntity.getUser(), taskEntity.getTitle(), taskEntity.getStatus());
        return taskMapper.toDTO(saved);
    }

    public PageResponse<TaskResponse> getAllTasks(Pageable pageable) {
        log.debug("Fetching all tasks requested.");
        return PageResponse.from(
                taskRepository.findAll(pageable)
                        .map(taskMapper::toDTO));
    }

    public PageResponse<TaskResponse> getAllUserTasks(Pageable pageable) {
        User user = getAuthenticatedUser();

        return PageResponse.from(
                taskRepository
                        .findTaskByUser(user, pageable)
                        .map(taskMapper::toDTO));
    }

    public TaskResponse getTaskById(Long id) {
        log.debug("Fetch task with id={}", id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals);

        if (isAdmin) {
            return taskRepository.findById(id)
                    .map(taskMapper::toDTO)
                    .orElseThrow(() -> new ResourceNotFoundException("task id not found"));
        }

        User user = getAuthenticatedUser();
        return taskRepository.findByIdAndUser(id, user)
                .map(taskMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("task id not found"));
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest updatedTask) {
        User user = getAuthenticatedUser();

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task Id not found"));

        log.debug("task update from {} to {}", task, taskMapper.toEntity(updatedTask));
        taskMapper.updateEntityFromDto(updatedTask, task);

        if (!Objects.equals(task.getUser().getId(), user.getId())) {
            throw new AccessDeniedException("User task not same as auth content");
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


    //find task by status function using query method
    public Page<TaskResponse> findTasksByStatus(String status, Pageable pageable) {
        User user = getAuthenticatedUser();

        status = status.toUpperCase();
        try {
            TaskStatus.valueOf(status);
            return taskRepository.findAllByStatusAndUser(TaskStatus.valueOf(status), user, pageable)
                    .map(taskMapper::toDTO);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid input { TaskStatus }");
        }
    }

    public Page<TaskResponse> findTaskByTitle(String title, Pageable pageable) {
        User user = getAuthenticatedUser();

        return taskRepository.findByTitleContainingIgnoreCaseAndUser(title, user, pageable)
                .map(taskMapper::toDTO);
    }

}
