package com.bad.batch.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChallengeSubmissionResponse {
    private Long id;
    private Long participationId;
    private String repositoryUrl;
    private String demoUrl;
    private String description;
    private String status;
    private LocalDateTime submittedAt;
    private Integer score;
    private String feedback;
    private Long reviewerId;
}

