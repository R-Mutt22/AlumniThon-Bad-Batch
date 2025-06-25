package com.bad.batch.model.entities;

import com.bad.batch.model.enums.ChallengeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("CHALLENGE")
@Data
@EqualsAndHashCode(callSuper = true)
public class Challenge extends Content {
    @Column(nullable = false, length = 5000)
    private String problemStatement;

    @Column(nullable = false, length = 2000)
    private String acceptanceCriteria;

    @Column(nullable = false)
    private Boolean allowsTeams = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private ChallengeType challengeType;
}

