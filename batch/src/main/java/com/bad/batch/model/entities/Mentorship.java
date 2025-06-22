package com.bad.batch.model.entities;

import com.bad.batch.model.enums.MentorshipType;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("MENTORSHIP")
public class Mentorship extends Content{
    private Boolean isLive;
    private String streamingUrl;
    private String meetingPassword;

    @Enumerated(EnumType.STRING)
    private MentorshipType type;

    private Integer durationMinutes;

    // Campos específicos para streaming
    private String streamingPlatform;
    private String streamingRoomId;
}
