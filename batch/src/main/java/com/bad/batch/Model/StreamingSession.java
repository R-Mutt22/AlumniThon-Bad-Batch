package com.bad.batch.Model;

import com.bad.batch.Enum.StreamingStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "streaming_sessions")
@Data
public class StreamingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentorship_id", nullable = false)
    private Mentorship mentorship;

    private String sessionId;
    private String streamingUrl;
    private String recordingUrl;

    @Enumerated(EnumType.STRING)
    private StreamingStatus status;

    private LocalDateTime scheduledStart;
    private LocalDateTime actualStart;
    private LocalDateTime endedAt;

    private Integer maxViewers;
    private Integer currentViewers = 0;

    private String streamingConfig;
}
