package com.bad.batch.Model;


import com.bad.batch.Enum.RankingType;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rankings")
@Data
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private RankingType type;

    private String category;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private Integer position;
    private Integer points;
    private BigDecimal score;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;
}
