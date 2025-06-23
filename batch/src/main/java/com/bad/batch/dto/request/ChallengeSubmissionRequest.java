package com.bad.batch.dto.request;

import lombok.Data;

@Data
public class ChallengeSubmissionRequest {
    private String repositoryUrl;
    private String demoUrl;
    private String description;
}

