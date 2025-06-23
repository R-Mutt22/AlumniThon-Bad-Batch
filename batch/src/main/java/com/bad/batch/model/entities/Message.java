package com.bad.batch.model.entities;

import com.bad.batch.model.enums.ChatMessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages", indexes = {
    @Index(name = "idx_sender_created", columnList = "sender_id, created_at"),
    @Index(name = "idx_recipient_created", columnList = "recipient_id, created_at"),
    @Index(name = "idx_challenge_created", columnList = "challenge_id, created_at"),
    @Index(name = "idx_mentorship_created", columnList = "mentorship_id, created_at"),
    @Index(name = "idx_conversation_type", columnList = "conversation_type, created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 1000)
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMessageType type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipient; // Para mensajes directos
    
    @Column(name = "challenge_id")
    private Long challengeId; // Para chats de challenges
    
    @Column(name = "mentorship_id") 
    private Long mentorshipId; // Para chats de mentoría
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isSystem = false;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isRead = false;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "edited_at")
    private LocalDateTime editedAt;
    
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    // Tipo de conversación para optimizar consultas
    @Column(name = "conversation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConversationType conversationType;
    
    public enum ConversationType {
        DIRECT,      // Mensaje directo entre dos usuarios
        CHALLENGE,   // Mensaje en chat de challenge
        MENTORSHIP   // Mensaje en chat de mentoría
    }
}
