package com.bad.batch.integration;

import com.bad.batch.dto.security.TokenResponse;
import com.bad.batch.dto.security.UserRegistrationRequest;
import com.bad.batch.model.enums.UserRole;
import com.bad.batch.repository.MessageRepository;
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
public class MessageIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private String authToken;
    private Long otherUserId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.deleteAll();
        messageRepository.deleteAll();
        
        // Crear primer usuario
        UserRegistrationRequest userRequest = new UserRegistrationRequest();
        userRequest.setFirstName("User");
        userRequest.setLastName("One");
        userRequest.setEmail("user1@example.com");
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

        // Crear segundo usuario
        UserRegistrationRequest otherUserRequest = new UserRegistrationRequest();
        otherUserRequest.setFirstName("User");
        otherUserRequest.setLastName("Two");
        otherUserRequest.setEmail("user2@example.com");
        otherUserRequest.setPassword("Password123!");
        otherUserRequest.setRole(UserRole.DEVELOPER);

        MvcResult otherRegistrationResult = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(otherUserRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String otherRegistrationResponse = otherRegistrationResult.getResponse().getContentAsString();
        TokenResponse otherTokenResponse = objectMapper.readValue(otherRegistrationResponse, TokenResponse.class);
        otherUserId = otherTokenResponse.getUser().getId();
    }

    @Test
    void testGetDirectMessages_Success() throws Exception {
        mockMvc.perform(get("/api/messages/direct/" + otherUserId)
                .header("Authorization", "Bearer " + authToken)
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testGetDirectMessages_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/messages/direct/" + otherUserId)
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetChallengeMessages_Success() throws Exception {
        Long challengeId = 1L; // ID simulado

        mockMvc.perform(get("/api/messages/challenge/" + challengeId)
                .header("Authorization", "Bearer " + authToken)
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testGetMentorshipMessages_Success() throws Exception {
        Long mentorshipId = 1L; // ID simulado

        mockMvc.perform(get("/api/messages/mentorship/" + mentorshipId)
                .header("Authorization", "Bearer " + authToken)
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testGetLastConversations_Success() throws Exception {
        mockMvc.perform(get("/api/messages/conversations")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetLastConversations_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/messages/conversations"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetUnreadCount_Success() throws Exception {
        mockMvc.perform(get("/api/messages/unread/count")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void testMarkMessageAsRead_Success() throws Exception {
        Long messageId = 1L; // ID simulado

        mockMvc.perform(put("/api/messages/" + messageId + "/read")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk());
    }

    @Test
    void testMarkConversationAsRead_Success() throws Exception {
        mockMvc.perform(put("/api/messages/conversation/" + otherUserId + "/read")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchMessages_Success() throws Exception {
        mockMvc.perform(get("/api/messages/search")
                .header("Authorization", "Bearer " + authToken)
                .param("query", "test")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testSearchMessages_WithChallengeId() throws Exception {
        Long challengeId = 1L;

        mockMvc.perform(get("/api/messages/search")
                .header("Authorization", "Bearer " + authToken)
                .param("query", "challenge")
                .param("challengeId", challengeId.toString())
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testSearchMessages_WithMentorshipId() throws Exception {
        Long mentorshipId = 1L;

        mockMvc.perform(get("/api/messages/search")
                .header("Authorization", "Bearer " + authToken)
                .param("query", "mentorship")
                .param("mentorshipId", mentorshipId.toString())
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testSearchMessages_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/messages/search")
                .param("query", "test"))
                .andExpect(status().isUnauthorized());
    }
}
