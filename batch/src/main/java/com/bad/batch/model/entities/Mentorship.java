package com.bad.batch.model.entities;

import com.bad.batch.model.enums.MentorshipType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("MENTORSHIP")
@Data
@EqualsAndHashCode(callSuper = true)
public class Mentorship extends Content {
    @Column(nullable = false)
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MentorshipType mentorshipType;

    @Column(nullable = false)
    private Boolean isLive = false;
}

