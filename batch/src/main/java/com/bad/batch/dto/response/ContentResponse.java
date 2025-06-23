package com.bad.batch.dto.response;

import com.bad.batch.model.enums.ChallengeType;
import com.bad.batch.model.enums.ContentType;
import com.bad.batch.model.enums.MentorshipType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ContentResponse {
    private Long id;
    private String title;
    private String description;
    private Long creatorId;
    private String creatorName;
    private String status;
    private String difficulty;
    private Set<String> requiredTechnologies;
    private Integer maxParticipants;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ContentType type;

    // Mentorship specific
    private Integer durationMinutes;
    private MentorshipType mentorshipType;
    private Boolean isLive;

    // Challenge specific
    private String problemStatement;
    private String acceptanceCriteria;
    private Boolean allowsTeams;
    private ChallengeType challengeType;
}
