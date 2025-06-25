package com.bad.batch.model.entities;

import com.bad.batch.model.enums.ExperienceLevel;
import com.bad.batch.model.enums.ProfileVisibility;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String bio;
    private String location;
    private String githubUrl;
    private String linkedinUrl;
    private String personalWebsite;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel; // JUNIOR, INTERMEDIATE, SENIOR

    @Enumerated(EnumType.STRING)
    private ProfileVisibility visibility; // PUBLIC, PRIVATE (RNF12)

    @ElementCollection
    @CollectionTable(name = "profile_technologies")
    private Set<String> technologies; // Java, Python, React, etc.

    @ElementCollection
    @CollectionTable(name = "profile_interests")
    private Set<String> interests; // Backend, Frontend, AI, etc.

    private String objectives; // Texto libre sobre objetivos

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
