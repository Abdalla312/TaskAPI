package com.example.taskapi.auth;

import com.example.taskapi.auth.dto.AuthRequest;
import com.example.taskapi.auth.dto.RefreshRequest;
import com.example.taskapi.auth.dto.RegisterRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;
    @Autowired RefreshTokenRepository refreshTokenRepository;

    // -----------------------------------------------------------------
    // DB cleanup — FK order: refresh_tokens -> users
    // -----------------------------------------------------------------
    @BeforeEach
    void cleanDatabase() {
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    // -----------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------

    private RegisterRequest buildRegisterRequest(String username) {
        RegisterRequest req = new RegisterRequest();
        req.setUsername(username);
        req.setEmail(username + "@test.com");
        req.setPassword("password123");
        return req;
    }

    /** Registers a user and returns the raw refresh token string. */
    private String registerAndGetRefreshToken(String username) throws Exception {
        String body = objectMapper.writeValueAsString(buildRegisterRequest(username));
        String response = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).at("/data/refreshToken").asText();
    }

    // =================================================================
    // POST /api/auth/register
    // =================================================================

    @Test
    void register_returns201_withValidBody() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRegisterRequest("alice"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("alice"));
    }

    @Test
    void register_returns409_whenUsernameAlreadyExists() throws Exception {
        // first registration succeeds
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRegisterRequest("bob"))))
                .andExpect(status().isCreated());

        // duplicate must conflict
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRegisterRequest("bob"))))
                .andExpect(status().isConflict());
    }

    @Test
    void register_returns400_whenPasswordTooShort() throws Exception {
        RegisterRequest req = buildRegisterRequest("carol");
        req.setPassword("short"); // violates @Size(min=8)

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returns400_whenEmailInvalid() throws Exception {
        RegisterRequest req = buildRegisterRequest("dave");
        req.setEmail("not-a-valid-email");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_returns400_whenUsernameTooShort() throws Exception {
        RegisterRequest req = buildRegisterRequest("dave");
        req.setUsername("ab"); // violates @Size(min=3)

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // =================================================================
    // POST /api/auth/login
    // =================================================================

    @Test
    void login_returns200_withValidCredentials() throws Exception {
        // set up user
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRegisterRequest("eve"))))
                .andExpect(status().isCreated());

        AuthRequest login = new AuthRequest();
        login.setIdentifier("eve");
        login.setPassword("password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("eve"));
    }

    @Test
    void login_returns200_usingEmailAsIdentifier() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRegisterRequest("frank"))))
                .andExpect(status().isCreated());

        // login with email instead of username
        AuthRequest login = new AuthRequest();
        login.setIdentifier("frank@test.com");
        login.setPassword("password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("frank"));
    }

    @Test
    void login_returns401_withWrongPassword() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRegisterRequest("grace"))))
                .andExpect(status().isCreated());

        AuthRequest login = new AuthRequest();
        login.setIdentifier("grace");
        login.setPassword("wrong_password");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_returns401_withUnknownUser() throws Exception {
        AuthRequest login = new AuthRequest();
        login.setIdentifier("ghost");
        login.setPassword("password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    // =================================================================
    // POST /api/auth/refresh
    // =================================================================

    @Test
    void refresh_returns200_withValidRefreshToken() throws Exception {
        String refreshToken = registerAndGetRefreshToken("henry");

        RefreshRequest req = new RefreshRequest();
        req.setRefreshToken(refreshToken);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
    }

    @Test
    void refresh_returns401_withInvalidToken() throws Exception {
        RefreshRequest req = new RefreshRequest();
        req.setRefreshToken("completely-invalid-token-string");

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void refresh_returns400_whenRefreshTokenBlank() throws Exception {
        // @NotBlank on RefreshRequest.refreshToken
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    // =================================================================
    // POST /api/auth/logout
    // =================================================================

    @Test
    void logout_returns200_andSubsequentRefreshIsRevoked() throws Exception {
        String refreshToken = registerAndGetRefreshToken("t_user");

        RefreshRequest req = new RefreshRequest();
        req.setRefreshToken(refreshToken);

        // logout succeeds
        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        // refresh with the same (now revoked) token must fail
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}
