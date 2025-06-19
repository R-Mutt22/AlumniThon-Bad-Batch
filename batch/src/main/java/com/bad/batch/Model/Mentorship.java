package com.bad.batch.Model;

import com.bad.batch.Enum.MentorshipType;
import jakarta.persistence.*;
import lombok.Data;

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
