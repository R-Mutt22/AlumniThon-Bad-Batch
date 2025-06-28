package com.bad.batch.websocket.dto;

import java.time.LocalDate;

import com.bad.batch.model.enums.ChatMessageType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageResponse {
    private Long id;
    private String content;
    private ChatMessageType type;
    private Long senderId; 
    private String senderName; // Nombre del remitente
    private Long recipientId; // Para mensajes directos
    private Long challengeId; // Para chats de challenges
    private Long mentorshipId; // Para chats de mentoría
    private LocalDateTime timestamp;
    private LocalDateTime readAt; // Cuándo fue leído el mensaje
    private boolean isSystem;
}
