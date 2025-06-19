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

    // Getters necesarios para los controladores
    public Boolean getIsLive() {
        return isLive;
    }
    public String getStreamingUrl() {
        return streamingUrl;
    }
    public String getMeetingPassword() {
        return meetingPassword;
    }
    public MentorshipType getType() {
        return type;
    }
    public Integer getDurationMinutes() {
        return durationMinutes;
    }
    public String getStreamingPlatform() {
        return streamingPlatform;
    }
    public String getStreamingRoomId() {
        return streamingRoomId;
    }

    // Setters necesarios para los controladores
    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }
    public void setStreamingUrl(String streamingUrl) {
        this.streamingUrl = streamingUrl;
    }
    public void setMeetingPassword(String meetingPassword) {
        this.meetingPassword = meetingPassword;
    }
    public void setType(MentorshipType type) {
        this.type = type;
    }
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    public void setStreamingPlatform(String streamingPlatform) {
        this.streamingPlatform = streamingPlatform;
    }
    public void setStreamingRoomId(String streamingRoomId) {
        this.streamingRoomId = streamingRoomId;
    }
}
