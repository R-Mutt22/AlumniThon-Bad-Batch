package com.bad.batch.Model;

import com.bad.batch.Enum.ChallengeType;
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
