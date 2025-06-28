package com.bad.batch.websocket.dto;

import com.bad.batch.model.enums.ChatMessageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatMessageRequest {
    @NotBlank(message = "El contenido del mensaje no puede estar vacío")
    private String content;

    @NotNull(message = "El tipo de mensaje es requerido")
    private ChatMessageType type = ChatMessageType.TEXT;

    private Long recipientId; // Para mensajes directos
    private Long challengeId; // Para chats de challenges
    private Long mentorshipId; // Para chats de mentoría
    
}
