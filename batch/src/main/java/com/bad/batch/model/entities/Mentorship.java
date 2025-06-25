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

    // Campos requeridos por la herencia de tabla única - usar valores por defecto para mentorías
    // Usar el mismo nombre que en Challenge para evitar conflictos
    @Column(nullable = false)
    private Boolean allowsTeams = false;
    
    @Column(nullable = false, length = 2000)
    private String acceptanceCriteria = "N/A - Mentorship";
    
    @Column(nullable = false, length = 5000)
    private String problemStatement = "N/A - Mentorship";
}

