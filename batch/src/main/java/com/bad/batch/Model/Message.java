package com.bad.batch.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.awt.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(length = 2000, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private TrayIcon.MessageType type;

    // Para chat de mentorías/desafíos
    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content relatedContent;

    private Boolean isRead = false;
    private LocalDateTime readAt;

    @CreationTimestamp
    private LocalDateTime sentAt;

    // Para mensajes editados/eliminados
    private Boolean isEdited = false;
    private Boolean isDeleted = false;
    private LocalDateTime editedAt;
}
