package com.bad.batch.Controller;

import com.bad.batch.Model.StreamingSession;
import com.bad.batch.Enum.StreamingStatus;
import com.bad.batch.Repository.StreamingSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/streaming-sessions")
public class StreamingSessionController {
    @Autowired
    private StreamingSessionRepository streamingSessionRepository;

    @GetMapping
    public List<StreamingSession> getAllStreamingSessions() {
        return streamingSessionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StreamingSession> getStreamingSessionById(@PathVariable Long id) {
        Optional<StreamingSession> session = streamingSessionRepository.findById(id);
        return session.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public StreamingSession createStreamingSession(@RequestBody StreamingSession session) {
        if (session.getStatus() == null) {
            session.setStatus(StreamingStatus.SCHEDULED);
        }
        return streamingSessionRepository.save(session);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StreamingSession> updateStreamingSession(@PathVariable Long id, @RequestBody StreamingSession details) {
        Optional<StreamingSession> optional = streamingSessionRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        StreamingSession session = optional.get();
        session.setMentorship(details.getMentorship());
        session.setSessionId(details.getSessionId());
        session.setStreamingUrl(details.getStreamingUrl());
        session.setRecordingUrl(details.getRecordingUrl());
        session.setStatus(details.getStatus());
        session.setScheduledStart(details.getScheduledStart());
        session.setActualStart(details.getActualStart());
        session.setEndedAt(details.getEndedAt());
        session.setMaxViewers(details.getMaxViewers());
        session.setCurrentViewers(details.getCurrentViewers());
        session.setStreamingConfig(details.getStreamingConfig());
        return ResponseEntity.ok(streamingSessionRepository.save(session));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStreamingSession(@PathVariable Long id) {
        if (!streamingSessionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        streamingSessionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

