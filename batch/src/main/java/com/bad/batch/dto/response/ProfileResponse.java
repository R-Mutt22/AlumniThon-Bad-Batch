package com.bad.batch.dto.response;

import com.bad.batch.model.enums.ExperienceLevel;
import com.bad.batch.model.enums.ProfileVisibility;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class ProfileResponse {
        private Long id;
        private Long userId;
        private String firstName;
        private String lastName;
        private String bio;
        private String location;
        private String githubUrl;
        private String linkedinUrl;
        private String personalWebsite;
        private ExperienceLevel experienceLevel;
        private ProfileVisibility visibility;
        private Set<String> technologies;
        private Set<String> interests;
        private String objectives;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
}
