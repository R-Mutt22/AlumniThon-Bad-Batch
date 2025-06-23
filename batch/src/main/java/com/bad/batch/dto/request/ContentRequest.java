package com.bad.batch.dto.request;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ContentRequest {
    private String title;
    private String description;
    private Long creatorId;
    private String status;
    private String difficulty;
    private Set<String> requiredTechnologies;
    private Integer maxParticipants;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}

