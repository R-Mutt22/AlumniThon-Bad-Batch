package com.bad.batch.model.entities;

import com.bad.batch.model.enums.ChallengeType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@DiscriminatorValue("CHALLENGE")
@Data
public class Challenge extends Content{
    @Column(length = 5000)
    private String problemStatement;

    @Column(length = 2000)
    private String acceptanceCriteria;

    private String repositoryTemplate;
    private Boolean allowsTeams;

    @Enumerated(EnumType.STRING)
    private ChallengeType type;
}
