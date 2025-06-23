package com.bad.batch.websocket.controller;

import com.bad.batch.websocket.dto.ChatMessageRequest;
import com.bad.batch.websocket.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    
    private final ChatService chatService;

    @MessageMapping("/chat.direct")
    public void sendDirectMessage(
            @Valid @Payload ChatMessageRequest request, 
            Principal principal) {
        
        Long senderId = extractUserIdFromPrincipal(principal);
        chatService.sendDirectMessage(senderId, request);
    }

    @MessageMapping("/chat.challenge")
    public void sendChallengeMessage(
            @Valid @Payload ChatMessageRequest request, 
            Principal principal) {
        
        Long senderId = extractUserIdFromPrincipal(principal);
        chatService.sendChallengeMessage(senderId, request);
    }

    @MessageMapping("/chat.mentorship")
    public void sendMentorshipMessage(
            @Valid @Payload ChatMessageRequest request, 
            Principal principal) {
        
        Long senderId = extractUserIdFromPrincipal(principal);
        chatService.sendMentorshipMessage(senderId, request);
    }

    @MessageMapping("/chat.join")
    public void joinChat(
            @Payload JoinChatRequest request, 
            Principal principal) {
        
        Long userId = extractUserIdFromPrincipal(principal);
        String destination = buildDestination(request);
        chatService.notifyUserJoined(userId, destination);
    }

    @MessageMapping("/chat.leave")
    public void leaveChat(
            @Payload JoinChatRequest request, 
            Principal principal) {
        
        Long userId = extractUserIdFromPrincipal(principal);
        String destination = buildDestination(request);
        chatService.notifyUserLeft(userId, destination);
    }

    private Long extractUserIdFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Usuario no autenticado");
        }
        return Long.valueOf(principal.getName());
    }

    private String buildDestination(JoinChatRequest request) {
        if (request.getChallengeId() != null) {
            return "/topic/challenge/" + request.getChallengeId();
        } else if (request.getMentorshipId() != null) {
            return "/topic/mentorship/" + request.getMentorshipId();
        }
        throw new IllegalArgumentException("Tipo de chat no válido");
    }

    public static class JoinChatRequest {
        private Long challengeId;
        private Long mentorshipId;
        
        // getters y setters
        public Long getChallengeId() { return challengeId; }
        public void setChallengeId(Long challengeId) { this.challengeId = challengeId; }
        public Long getMentorshipId() { return mentorshipId; }
        public void setMentorshipId(Long mentorshipId) { this.mentorshipId = mentorshipId; }
    }
}
