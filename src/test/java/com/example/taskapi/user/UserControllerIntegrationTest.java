package com.example.taskapi.user;

import com.example.taskapi.auth.RefreshTokenRepository;
import com.example.taskapi.auth.dto.AuthRequest;
import com.example.taskapi.auth.dto.RegisterRequest;
import com.example.taskapi.user.dto.UserRequest;
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
class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void cleanDatabase() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    //Helpers
    private RegisterRequest buildRegisterRequest(String username) {
        RegisterRequest req = new RegisterRequest();
        req.setUsername(username);
        req.setEmail(username + "@test.com");
        req.setPassword("password123");
        return req;
    }

    // register a user and return their access token
    private String registerAndGetToken(String username) throws Exception {
        String body = objectMapper.writeValueAsString(buildRegisterRequest(username));
        String response = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).at("/data/accessToken").asText();
    }

    private String registerAsAdminAndGetToken(String username) throws Exception {
        registerAndGetToken(username);
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

    private Long getUserId(String username) {
        return userRepository.findByUsernameOrEmail(username)
                .orElseThrow()
                .getId();
    }

    // GET api/v1/users/me for fetching current profile {200, 401, 404, 500}
    @Test
    void getMyUserDetails_returns200_whenUserExists() throws Exception {
        String token = registerAndGetToken("test_user");
        mockMvc.perform(get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("test_user"));
    }

    @Test
    void getMyUserDetails_returns401_whenNoToken() throws Exception {
        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isUnauthorized());
    }

    // PATCH api/v1/users/me to edit users data {200, 401, 404, 500}
    @Test
    void patchMyUserDetails_returns200_whenAuthenticated() throws Exception {
        String token = registerAndGetToken("test_patch");
        UserRequest request = new UserRequest();
        request.setUsername("test_user_patched");
        mockMvc.perform(patch("/api/v1/users/me")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.username").value("test_user_patched"));
    }

    @Test
    void patchMyUserDetails_returns401_whenNoToken() throws Exception {

        UserRequest request = new UserRequest();
        request.setUsername("Unauthorized_username");
        mockMvc.perform(patch("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    // GET api/v1/users to list users - for admins only {200, 401, 403, 500}
    @Test
    void listUsers_returns200_whenAdminAuthenticated() throws Exception {
        registerAndGetToken("user");
        String adminToken = registerAsAdminAndGetToken("admin_user");
        mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.totalElements").value(2));
    }

    @Test
    void listUsers_returns403_whenUserAuthenticated() throws Exception {
        String userToken = registerAndGetToken("user");
        mockMvc.perform(get("/api/v1/users")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void listUsers_returns401_whenNoToken() throws Exception {
        registerAndGetToken("user");
        registerAsAdminAndGetToken("admin_user");
        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isUnauthorized());
    }

    // GET api/v1/users/{id} to fetch certain user with id param {200, 401, 403, 404, 500} -  for admins only
    @Test
    void getUserById_returns200_whenAdminAuthenticated() throws Exception {
        registerAndGetToken("target_user");
        Long userId = getUserId("target_user");
        String adminToken = registerAsAdminAndGetToken("admin_get_user");

        mockMvc.perform(get("/api/v1/users/" + userId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userId));
    }

    @Test
    void getUserById_returns403_whenUserAuthenticated() throws Exception {
        registerAndGetToken("target_user_403");
        Long userId = getUserId("target_user_403");
        String userToken = registerAndGetToken("regular_get_user");

        mockMvc.perform(get("/api/v1/users/" + userId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getUserById_returns404_whenUserNotFound() throws Exception {
        String adminToken = registerAsAdminAndGetToken("admin_get_missing");

        mockMvc.perform(get("/api/v1/users/99999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    // DELETE api/v1/users/{id} to delete user with id param  {200, 404, 500} - for admins only
    @Test
    void deleteUser_returns204_whenAdminAuthenticated() throws Exception {
        registerAndGetToken("delete_target");
        Long userId = getUserId("delete_target");
        String adminToken = registerAsAdminAndGetToken("admin_delete_user");

        mockMvc.perform(delete("/api/v1/users/" + userId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_returns403_whenUserAuthenticated() throws Exception {
        registerAndGetToken("delete_target_403");
        Long userId = getUserId("delete_target_403");
        String userToken = registerAndGetToken("regular_delete_user");

        mockMvc.perform(delete("/api/v1/users/" + userId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUser_returns404_whenUserNotFound() throws Exception {
        String adminToken = registerAsAdminAndGetToken("admin_delete_missing");

        mockMvc.perform(delete("/api/v1/users/99999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    // PATCH api/v1/users/admin/{id} to change user role to admin  {200, 401, 403, 404, 500} - for admins only
    @Test
    void makeAdmin_returns200_whenAdminAuthenticated() throws Exception {
        registerAndGetToken("promote_user");
        Long userId = getUserId("promote_user");
        String adminToken = registerAsAdminAndGetToken("admin_promote");

        mockMvc.perform(patch("/api/v1/users/admin/" + userId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.role").value("ADMIN"));
    }

    @Test
    void makeAdmin_returns403_whenUserAuthenticated() throws Exception {
        registerAndGetToken("promote_user_403");
        Long userId = getUserId("promote_user_403");
        String userToken = registerAndGetToken("regular_promote");

        mockMvc.perform(patch("/api/v1/users/admin/" + userId)
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void makeAdmin_returns404_whenUserNotFound() throws Exception {
        String adminToken = registerAsAdminAndGetToken("admin_promote_missing");

        mockMvc.perform(patch("/api/v1/users/admin/99999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

}
