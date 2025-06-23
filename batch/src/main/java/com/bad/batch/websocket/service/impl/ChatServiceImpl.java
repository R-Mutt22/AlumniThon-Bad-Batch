package com.bad.batch.websocket.service.impl;

import com.bad.batch.model.entities.Message;
import com.bad.batch.model.entities.User;
import com.bad.batch.repository.MessageRepository;
import com.bad.batch.repository.UserRepository;
import com.bad.batch.websocket.dto.ChatMessageRequest;
import com.bad.batch.websocket.dto.ChatMessageResponse;
import com.bad.batch.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {
    
    private final SimpMessagingTemplate messagingTemplate;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public ChatMessageResponse sendDirectMessage(Long senderId, ChatMessageRequest request) {
        User sender = getUserById(senderId);
        User recipient = request.getRecipientId() != null ? getUserById(request.getRecipientId()) : null;
        
        // 1. Guardar mensaje en base de datos
        Message message = Message.builder()
                .content(request.getContent())
                .type(request.getType())
                .sender(sender)
                .recipient(recipient)
                .conversationType(Message.ConversationType.DIRECT)
                .build();
        
        Message savedMessage = messageRepository.save(message);
        
        // 2. Construir respuesta
        ChatMessageResponse response = buildChatMessageFromEntity(savedMessage);
        
        // 3. Enviar por WebSocket a ambos usuarios
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
        
        log.info("Mensaje directo guardado y enviado de {} a {}", senderId, request.getRecipientId());
        return response;
    }

    @Override
    @Transactional
    public ChatMessageResponse sendChallengeMessage(Long senderId, ChatMessageRequest request) {
        User sender = getUserById(senderId);
        
        // 1. Guardar mensaje en base de datos
        Message message = Message.builder()
                .content(request.getContent())
                .type(request.getType())
                .sender(sender)
                .challengeId(request.getChallengeId())
                .conversationType(Message.ConversationType.CHALLENGE)
                .build();
        
        Message savedMessage = messageRepository.save(message);
        
        // 2. Construir respuesta
        ChatMessageResponse response = buildChatMessageFromEntity(savedMessage);
        
        // 3. Enviar por WebSocket a todos los participantes del challenge
        messagingTemplate.convertAndSend(
            "/topic/challenge/" + request.getChallengeId(), 
            response
        );
        
        log.info("Mensaje de challenge guardado y enviado por {} en challenge {}", senderId, request.getChallengeId());
        return response;
    }

    @Override
    @Transactional
    public ChatMessageResponse sendMentorshipMessage(Long senderId, ChatMessageRequest request) {
        User sender = getUserById(senderId);
        
        // 1. Guardar mensaje en base de datos
        Message message = Message.builder()
                .content(request.getContent())
                .type(request.getType())
                .sender(sender)
                .mentorshipId(request.getMentorshipId())
                .conversationType(Message.ConversationType.MENTORSHIP)
                .build();
        
        Message savedMessage = messageRepository.save(message);
        
        // 2. Construir respuesta
        ChatMessageResponse response = buildChatMessageFromEntity(savedMessage);
        
        // 3. Enviar por WebSocket a todos los participantes de la mentoría
        messagingTemplate.convertAndSend(
            "/topic/mentorship/" + request.getMentorshipId(), 
            response
        );
        
        log.info("Mensaje de mentoría guardado y enviado por {} en mentoría {}", senderId, request.getMentorshipId());
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

    // Helper method para convertir Message entity a ChatMessageResponse
    private ChatMessageResponse buildChatMessageFromEntity(Message message) {
        return ChatMessageResponse.builder()
            .id(message.getId())
            .content(message.getContent())
            .type(message.getType())
            .senderId(message.getSender().getId())
            .senderName(message.getSender().getFirstName() + " " + message.getSender().getLastName())
            .recipientId(message.getRecipient() != null ? message.getRecipient().getId() : null)
            .challengeId(message.getChallengeId())
            .mentorshipId(message.getMentorshipId())
            .timestamp(message.getCreatedAt())
            .isSystem(message.getIsSystem())
            .build();
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + userId));
    }
}
