package com.bad.batch.Model;

import com.bad.batch.Enum.SubmissionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "challenge_submissions")
@Data
public class ChallengeSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participation_id", nullable = false)
    private Participation participation;

    private String repositoryUrl;
    private String demoUrl;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;

    private Integer score;
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;
}
