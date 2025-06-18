package com.bad.batch.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_stats")
@Data
public class UserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Estadísticas de desafíos
    private Integer challengesCompleted = 0;
    private Integer challengesCreated = 0;
    private BigDecimal averageChallengeScore = BigDecimal.ZERO;
    private Integer totalChallengeScore = 0;

    // Estadísticas de mentorías
    private Integer mentorshipsAttended = 0;
    private Integer mentorshipsCreated = 0;
    private Integer mentorshipsCompleted = 0;

    // Ranking general
    private Integer globalRank;
    private Integer totalPoints = 0;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
