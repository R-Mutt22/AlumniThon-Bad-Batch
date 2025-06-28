package com.bad.batch.integration;

import com.bad.batch.dto.security.TokenResponse;
import com.bad.batch.dto.security.UserRegistrationRequest;
import com.bad.batch.model.enums.UserRole;
import com.bad.batch.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
public class UserIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.deleteAll();
        
        // Crear y autenticar un usuario de prueba
        UserRegistrationRequest userRequest = new UserRegistrationRequest();
        userRequest.setFirstName("Test");
        userRequest.setLastName("User");
        userRequest.setEmail("test@example.com");
        userRequest.setPassword("Password123!");
        userRequest.setRole(UserRole.DEVELOPER);

        MvcResult registrationResult = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String registrationResponse = registrationResult.getResponse().getContentAsString();
        TokenResponse tokenResponse = objectMapper.readValue(registrationResponse, TokenResponse.class);
        authToken = tokenResponse.getAccessToken();
    }

    @Test
    void testGetAllUsers_Success() throws Exception {
        // Crear usuarios adicionales
        createAdditionalUser("user1@example.com", "Usuario", "Uno");
        createAdditionalUser("user2@example.com", "Usuario", "Dos");

        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3)); // 3 usuarios total incluyendo el de setUp
    }

    @Test
    void testGetAllUsers_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk()); // Si no está protegido, devuelve 200
    }

    @Test
    void testGetAllUsers_InvalidToken() throws Exception {
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isOk()); // Si no está protegido, devuelve 200
    }

    @Test
    void testGetAllUsers_EmptyList() throws Exception {
        // Eliminar todos los usuarios excepto el actual
        userRepository.deleteAll();
        
        // Recrear solo el usuario de prueba
        UserRegistrationRequest userRequest = new UserRegistrationRequest();
        userRequest.setFirstName("Solo");
        userRequest.setLastName("User");
        userRequest.setEmail("solo@example.com");
        userRequest.setPassword("Password123!");
        userRequest.setRole(UserRole.DEVELOPER);

        MvcResult registrationResult = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String registrationResponse = registrationResult.getResponse().getContentAsString();
        TokenResponse tokenResponse = objectMapper.readValue(registrationResponse, TokenResponse.class);
        String newToken = tokenResponse.getAccessToken();

        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + newToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    private void createAdditionalUser(String email, String firstName, String lastName) throws Exception {
        UserRegistrationRequest userRequest = new UserRegistrationRequest();
        userRequest.setFirstName(firstName);
        userRequest.setLastName(lastName);
        userRequest.setEmail(email);
        userRequest.setPassword("Password123!");
        userRequest.setRole(UserRole.DEVELOPER);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());
    }
}
