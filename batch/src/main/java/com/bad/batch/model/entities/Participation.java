package com.bad.batch.model.entities;

import com.bad.batch.model.enums.ParticipationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "participations")
@Data
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User participant;

    @ManyToOne
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus status; // JOINED, IN_PROGRESS, COMPLETED, DROPPED

    private LocalDateTime joinedAt;
    private LocalDateTime completedAt;

    // Para desafíos
    @OneToMany(mappedBy = "participation", cascade = CascadeType.ALL)
    private Set<ChallengeSubmission> submissions;

    // Para progreso en mentorías
    @Column(scale = 2)
    private BigDecimal progressPercentage;

    private String notes;
}
