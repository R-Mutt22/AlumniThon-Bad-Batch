package com.bad.batch.dto.response;

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
}

