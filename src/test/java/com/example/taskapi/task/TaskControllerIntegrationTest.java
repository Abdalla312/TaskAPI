package com.example.taskapi.task;

import com.example.taskapi.auth.RefreshTokenRepository;
import com.example.taskapi.auth.dto.AuthRequest;
import com.example.taskapi.auth.dto.RegisterRequest;
import com.example.taskapi.task.dto.TaskRequest;
import com.example.taskapi.user.Role;
import com.example.taskapi.user.User;
import com.example.taskapi.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired TaskRepository taskRepository;
    @Autowired RefreshTokenRepository refreshTokenRepository;

    // DB cleanup — FK order: refresh_tokens, tasks -> users
    @BeforeEach
    void cleanDatabase() {
        refreshTokenRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    // Helpers

    private RegisterRequest buildRegisterRequest(String username) {
        RegisterRequest req = new RegisterRequest();
        req.setUsername(username);
        req.setEmail(username + "@test.com");
        req.setPassword("password123");
        return req;
    }

    /** Registers a USER and returns their access token. */
    private String registerAndGetToken(String username) throws Exception {
        String body = objectMapper.writeValueAsString(buildRegisterRequest(username));
        String response = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).at("/data/accessToken").asText();
    }

    /**
     * Registers a user, promotes them to ADMIN directly in the DB,
     * then re-logs in so the returned JWT carries the ADMIN role.
     */
    private String registerAsAdminAndGetToken(String username) throws Exception {
        registerAndGetToken(username); // creates the user row

        User user = userRepository.findByUsernameOrEmail(username).orElseThrow();
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        AuthRequest login = new AuthRequest();
        login.setIdentifier(username);
        login.setPassword("password123");

        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).at("/data/accessToken").asText();
    }

    /** Creates a task via the API and returns its id from the response body. */
    private long createTaskAndGetId(String accessToken) throws Exception {
        TaskRequest req = new TaskRequest();
        req.setTitle("My Task");
        req.setDescription("Some description");
        req.setTaskStatus("TODO");

        String response = mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).at("/data/id").asLong();
    }

    // =================================================================
    // POST /api/tasks
    // =================================================================

    @Test
    void createTask_returns201_whenAuthenticated() throws Exception {
        String token = registerAndGetToken("user1");

        TaskRequest req = new TaskRequest();
        req.setTitle("Buy groceries");
        req.setDescription("Milk, eggs, bread");
        req.setTaskStatus("TODO");

        mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.title").value("Buy groceries"))
                .andExpect(jsonPath("$.data.taskStatus").value("TODO"));
    }

    @Test
    void createTask_returns401_whenNoToken() throws Exception {
        TaskRequest req = new TaskRequest();
        req.setTitle("Unauthorized task");
        req.setDescription("desc");
        req.setTaskStatus("TODO");

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createTask_returns400_whenRequiredFieldsMissing() throws Exception {
        String token = registerAndGetToken("user2");

        // Empty body violates @NotBlank constraints in the OnCreate group
        mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTask_returns400_whenStatusValueInvalid() throws Exception {
        String token = registerAndGetToken("user3");

        TaskRequest req = new TaskRequest();
        req.setTitle("Bad status task");
        req.setDescription("desc");
        req.setTaskStatus("INVALID_STATUS"); // violates @Pattern

        mockMvc.perform(post("/api/v1/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // =================================================================
    // GET /api/tasks   (current user's tasks)
    // =================================================================

    @Test
    void getMyTasks_returns200_withPaginatedResults() throws Exception {
        String token = registerAndGetToken("user4");
        createTaskAndGetId(token);
        createTaskAndGetId(token);

        mockMvc.perform(get("/api/v1/tasks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    void getMyTasks_doesNotReturnOtherUsersTask() throws Exception {
        String tokenA = registerAndGetToken("userA");
        String tokenB = registerAndGetToken("userB");
        createTaskAndGetId(tokenA); // task belongs to userA

        // userB should see 0 tasks
        mockMvc.perform(get("/api/v1/tasks")
                        .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(0));
    }

    // =================================================================
    // GET /api/tasks/{id}
    // =================================================================

    @Test
    void getTaskById_returns200_whenTaskExists() throws Exception {
        String token = registerAndGetToken("user5");
        long taskId = createTaskAndGetId(token);

        mockMvc.perform(get("/api/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(taskId));
    }

    @Test
    void getTaskById_returns404_whenTaskNotFound() throws Exception {
        String token = registerAndGetToken("user6");

        mockMvc.perform(get("/api/v1/tasks/99999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    // =================================================================
    // GET /api/tasks?status=
    // =================================================================

    @Test
    void getTasksByStatus_returns200_withMatchingTasks() throws Exception {
        String token = registerAndGetToken("user7");
        createTaskAndGetId(token); // status defaults to TODO

        mockMvc.perform(get("/api/v1/tasks")
                        .param("status", "TODO")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void getTasksByStatus_returns400_withInvalidStatus() throws Exception {
        String token = registerAndGetToken("user8");

        mockMvc.perform(get("/api/v1/tasks")
                        .param("status", "FLYING")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    // =================================================================
    // PATCH /api/tasks/{id}
    // =================================================================

    @Test
    void updateTask_returns200_whenOwnerPatches() throws Exception {
        String token = registerAndGetToken("user9");
        long taskId = createTaskAndGetId(token);

        TaskRequest patch = new TaskRequest();
        patch.setTitle("Updated title");

        mockMvc.perform(patch("/api/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated title"));
    }

    @Test
    void updateTask_returns403_whenNotOwner() throws Exception {
        String ownerToken = registerAndGetToken("owner1");
        String otherToken = registerAndGetToken("other1");
        long taskId = createTaskAndGetId(ownerToken);

        TaskRequest patch = new TaskRequest();
        patch.setTitle("Stolen update");

        // otherToken tries to patch owner's task
        mockMvc.perform(patch("/api/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + otherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateTask_returns400_whenInvalidStatusInPatch() throws Exception {
        String token = registerAndGetToken("user10");
        long taskId = createTaskAndGetId(token);

        TaskRequest patch = new TaskRequest();
        patch.setTaskStatus("BOGUS"); // violates @Pattern

        mockMvc.perform(patch("/api/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isBadRequest());
    }

    // =================================================================
    // DELETE /api/tasks/{id}   (ADMIN only)
    // =================================================================

    @Test
    void deleteTask_returns204_whenAdmin() throws Exception {
        String userToken  = registerAndGetToken("victim1");
        String adminToken = registerAsAdminAndGetToken("admin1");
        long taskId = createTaskAndGetId(userToken);

        mockMvc.perform(delete("/api/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_returns403_whenRegularUser() throws Exception {
        String token = registerAndGetToken("user11");
        long taskId = createTaskAndGetId(token);

        // regular user cannot delete — ADMIN role required
        mockMvc.perform(delete("/api/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteTask_returns404_whenTaskDoesNotExist() throws Exception {
        String adminToken = registerAsAdminAndGetToken("admin2");

        mockMvc.perform(delete("/api/v1/tasks/99999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    // =================================================================
    // GET /api/tasks/admin-all   (ADMIN only)
    // =================================================================

    @Test
    void adminAll_returns200_whenAdmin() throws Exception {
        String userToken  = registerAndGetToken("user12");
        String adminToken = registerAsAdminAndGetToken("admin3");
        createTaskAndGetId(userToken);

        mockMvc.perform(get("/api/v1/tasks/admin-all")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void adminAll_returns403_whenRegularUser() throws Exception {
        String token = registerAndGetToken("user13");

        mockMvc.perform(get("/api/v1/tasks/admin-all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
