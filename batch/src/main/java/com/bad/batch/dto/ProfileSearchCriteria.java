package com.bad.batch.dto;

import com.bad.batch.model.enums.ExperienceLevel;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
@Data
@Builder
public class ProfileSearchCriteria {
    private String query;
    private Set<String> technologies;
    private ExperienceLevel experienceLevel;
    private String location;
    private Set<String> interests;
    private int page = 0;
    private int size = 20;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";
}
