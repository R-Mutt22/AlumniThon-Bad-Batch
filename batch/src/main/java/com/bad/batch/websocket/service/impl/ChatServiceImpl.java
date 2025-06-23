package com.bad.batch.websocket.service.impl;

import com.bad.batch.model.entities.User;
import com.bad.batch.repository.UserRepository;
import com.bad.batch.websocket.dto.ChatMessageRequest;
import com.bad.batch.websocket.dto.ChatMessageResponse;
import com.bad.batch.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;

    @Override
    public ChatMessageResponse sendDirectMessage(Long senderId, ChatMessageRequest request) {
        User sender = getUserById(senderId);
        
        ChatMessageResponse response = buildChatMessage(request, sender);
        response.setRecipientId(request.getRecipientId());
        
        // Enviar a ambos usuarios
        messagingTemplate.convertAndSendToUser(
            senderId.toString(), 
            "/queue/messages", 
            response
        );
        
        if (request.getRecipientId() != null) {
            messagingTemplate.convertAndSendToUser(
                request.getRecipientId().toString(), 
                "/queue/messages", 
                response
            );
        }
        
        log.info("Mensaje directo enviado de {} a {}", senderId, request.getRecipientId());
        return response;
    }

    @Override
    public ChatMessageResponse sendChallengeMessage(Long senderId, ChatMessageRequest request) {
        User sender = getUserById(senderId);
        
        ChatMessageResponse response = buildChatMessage(request, sender);
        response.setChallengeId(request.getChallengeId());
        
        // Enviar a todos los participantes del challenge
        messagingTemplate.convertAndSend(
            "/topic/challenge/" + request.getChallengeId(), 
            response
        );
        
        log.info("Mensaje de challenge enviado por {} en challenge {}", senderId, request.getChallengeId());
        return response;
    }

    @Override
    public ChatMessageResponse sendMentorshipMessage(Long senderId, ChatMessageRequest request) {
        User sender = getUserById(senderId);
        
        ChatMessageResponse response = buildChatMessage(request, sender);
        response.setMentorshipId(request.getMentorshipId());
        
        // Enviar a todos los participantes de la mentoría
        messagingTemplate.convertAndSend(
            "/topic/mentorship/" + request.getMentorshipId(), 
            response
        );
        
        log.info("Mensaje de mentoría enviado por {} en mentoría {}", senderId, request.getMentorshipId());
        return response;
    }

    @Override
    public void notifyUserJoined(Long userId, String destination) {
        User user = getUserById(userId);
        
        ChatMessageResponse notification = ChatMessageResponse.builder()
            .content(user.getFirstName() + " se ha unido al chat")
            .senderId(userId)
            .senderName(user.getFirstName() + " " + user.getLastName())
            .timestamp(LocalDateTime.now())
            .isSystem(true)
            .build();
            
        messagingTemplate.convertAndSend(destination, notification);
        log.info("Usuario {} se unió a {}", userId, destination);
    }

    @Override
    public void notifyUserLeft(Long userId, String destination) {
        User user = getUserById(userId);
        
        ChatMessageResponse notification = ChatMessageResponse.builder()
            .content(user.getFirstName() + " ha salido del chat")
            .senderId(userId)
            .senderName(user.getFirstName() + " " + user.getLastName())
            .timestamp(LocalDateTime.now())
            .isSystem(true)
            .build();
            
        messagingTemplate.convertAndSend(destination, notification);
        log.info("Usuario {} salió de {}", userId, destination);
    }

    private ChatMessageResponse buildChatMessage(ChatMessageRequest request, User sender) {
        return ChatMessageResponse.builder()
            .content(request.getContent())
            .type(request.getType())
            .senderId(sender.getId())
            .senderName(sender.getFirstName() + " " + sender.getLastName())
            .timestamp(LocalDateTime.now())
            .isSystem(false)
            .build();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));
    }
}
