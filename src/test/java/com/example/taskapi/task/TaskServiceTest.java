package com.example.taskapi.task;

import com.example.taskapi.common.exception.BadRequestException;
import com.example.taskapi.common.exception.ResourceNotFoundException;
import com.example.taskapi.task.dto.TaskRequest;
import com.example.taskapi.task.dto.TaskResponse;
import com.example.taskapi.user.User;
import com.example.taskapi.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private TaskMapper taskMapper;
    @Mock private UserRepository userRepository;
    @InjectMocks private TaskService taskService;

    private final String authUsername = "test_user";
    private SecurityContext previousContext;

    @BeforeEach
    void setUp() {
        // Preserve and replace the security context so tests don't interfere with other tests or real security
        previousContext = SecurityContextHolder.getContext();

        SecurityContext ctx = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);

        when(ctx.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn(authUsername);

        SecurityContextHolder.setContext(ctx);
    }

    @AfterEach
    void tearDown() {
        //restore previous context
        SecurityContextHolder.setContext(previousContext);
        Mockito.reset(taskRepository, userRepository, taskMapper);
    }

    // Helper builders
    private User makeUser(Long id) {
        User u = new User();
        u.setId(id);
        u.setUsername(authUsername);
        u.setEmail("t@example.com");
        return u;
    }

    private Task makeTask(Long id, User user, String title) {
        Task t = new Task();
        t.setId(id);
        t.setTitle(title);
        t.setDescription("desc");
        t.setStatus(TaskStatus.TODO);
        t.setUser(user);
        return t;
    }

    private TaskRequest makeTaskRequest(String title) {
        TaskRequest request = new TaskRequest();
        request.setTitle(title);
        request.setDescription("desc");
        return request;
    }

    private TaskResponse makeTaskResponse(Long id, String title) {
        TaskResponse response = new TaskResponse();
        response.setId(id);
        response.setTitle(title);
        response.setDescription("desc");
        response.setTaskStatus("TODO");
        return response;
    }

    @Test
    void should_createTask_when_validInput() {
        User user = makeUser(1L);
        TaskRequest request = makeTaskRequest("NewTask");
        Task taskEntity = new Task();
        taskEntity.setTitle(request.getTitle());
        taskEntity.setDescription(request.getDescription());

        Task saved = makeTask(10L, user, request.getTitle());
        TaskResponse response = makeTaskResponse(10L, request.getTitle());

        // stubbing mapper and user repo and repo.save
        when(taskMapper.toEntity(request)).thenReturn(taskEntity);
        when(userRepository.findByUsernameOrEmail(authUsername)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(saved);
        when(taskMapper.toDTO(saved)).thenReturn(response);

        TaskResponse result = taskService.createTask(request);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getTitle()).isEqualTo("NewTask");
        // verify the repository save was called with an entity that has the user set
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());
        assertThat(captor.getValue().getUser()).isEqualTo(user);

    }

    @Test
    void should_throwException_when_taskNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> taskService.getTaskById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("task id not found");
        verify(taskRepository).findById(999L);
    }

    @Test
    void should_returnFilteredTasks_when_statusProvided() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = makeUser(1L);

        Task t1 = makeTask(1L, user, "t1");
        Task t2 = makeTask(2L, user, "t2");

        // map tasks to responses
        TaskResponse r1 = makeTaskResponse(1L, "t1");
        TaskResponse r2 = makeTaskResponse(2L, "t2");

        when(taskRepository.findAllByStatus(TaskStatus.TODO, pageable))
                .thenReturn(new PageImpl<>(List.of(t1, t2), pageable, 2));
        when(taskMapper.toDTO(t1)).thenReturn(r1);
        when(taskMapper.toDTO(t2)).thenReturn(r2);
        var page = taskService.findTasksByStatus("todo", pageable);

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getTitle()).isEqualTo("t1");
    }

    @Test
    void should_throwBadRequest_when_invalidStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        // method should throw BadRequestException for invalid status string
        assertThatThrownBy(() -> taskService.findTasksByStatus("invalid_status", pageable))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Invalid input");
    }

    @Test
    void should_updateTask_successfully() {
        // prepare authenticated user, existing task and request
        User user = makeUser(1L);
        when(userRepository.findByUsernameOrEmail(authUsername)).thenReturn(Optional.of(user));

        Task existing = makeTask(5L, user, "old");
        when(taskRepository.findById(5L)).thenReturn(Optional.of(existing));

        TaskRequest updateRequest = makeTaskRequest("updated");
        // taskMapper.updateEntityFromDto will be called; we can let it be a no-op or simulate changing the title
        doAnswer(invocation -> {
            TaskRequest request = invocation.getArgument(0);
            Task target = invocation.getArgument(1);
            target.setTitle(request.getTitle());
            return null;
        }).when(taskMapper).updateEntityFromDto(eq(updateRequest),any(Task.class));

        when(taskMapper.toDTO(existing)).thenReturn(makeTaskResponse(5L, "updated"));
        TaskResponse updated = taskService.updateTask(5L, updateRequest);
        assertThat(updated.getTitle()).isEqualTo("updated");
        verify(taskRepository).save(existing);
    }

    @Test
    void should_deleteTask_successfully() {
        Task existing = makeTask(7L, makeUser(1L), "to-delete");
        when(taskRepository.findById(7L)).thenReturn(Optional.of(existing));

        taskService.deleteTask(7L);

        verify(taskRepository).deleteById(7L);
    }
}
