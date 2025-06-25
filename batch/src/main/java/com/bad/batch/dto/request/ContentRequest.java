package com.bad.batch.dto.request;

import com.bad.batch.model.enums.ChallengeType;
import com.bad.batch.model.enums.ContentType;
import com.bad.batch.model.enums.MentorshipType;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ContentRequest {
    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 2000)
    private String description;

    @NotNull
    private Long creatorId;

    private String status;
    private String difficulty;

    @NotNull
    @Size(max = 5)
    private Set<@NotBlank String> requiredTechnologies;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer maxParticipants;

    @NotNull
    @Future
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private ContentType type;

    // Mentorship specific
    @Min(30)
    @Max(480)
    private Integer durationMinutes;

    private MentorshipType mentorshipType;
    private Boolean isLive = false;

    // Challenge specific
    @Size(max = 5000)
    private String problemStatement;

    @Size(max = 2000)
    private String acceptanceCriteria;

    private Boolean allowsTeams = false;
    private ChallengeType challengeType;
}
