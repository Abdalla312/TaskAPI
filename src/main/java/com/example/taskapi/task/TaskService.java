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

    public TaskResponse createTask(TaskRequest task) {
        Task taskEntity = taskMapper.toEntity(task);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Authentication"));
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
        //get user object from auth Context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsernameOrEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Authentication"));

        //return pageResponse
        return PageResponse.from(
                taskRepository
                        .findTaskByUser(user, pageable)
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsernameOrEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Authentication"));

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
