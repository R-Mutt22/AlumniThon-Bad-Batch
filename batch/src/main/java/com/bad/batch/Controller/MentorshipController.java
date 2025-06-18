package com.bad.batch.Controller;

import com.bad.batch.Model.Mentorship;
import com.bad.batch.Enum.MentorshipType;
import com.bad.batch.Repository.MentorshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mentorships")
public class MentorshipController {
    @Autowired
    private MentorshipRepository mentorshipRepository;

    @GetMapping
    public List<Mentorship> getAllMentorships() {
        return mentorshipRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mentorship> getMentorshipById(@PathVariable int id) {
        Optional<Mentorship> mentorship = mentorshipRepository.findById(id);
        return mentorship.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mentorship createMentorship(@RequestBody Mentorship mentorship) {
        if (mentorship.getType() == null) {
            mentorship.setType(MentorshipType.GENERAL);
        }
        return mentorshipRepository.save(mentorship);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mentorship> updateMentorship(@PathVariable int id, @RequestBody Mentorship details) {
        Optional<Mentorship> optional = mentorshipRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Mentorship mentorship = optional.get();
        mentorship.setIsLive(details.getIsLive());
        mentorship.setStreamingUrl(details.getStreamingUrl());
        mentorship.setMeetingPassword(details.getMeetingPassword());
        mentorship.setType(details.getType());
        mentorship.setDurationMinutes(details.getDurationMinutes());
        mentorship.setStreamingPlatform(details.getStreamingPlatform());
        mentorship.setStreamingRoomId(details.getStreamingRoomId());
        return ResponseEntity.ok(mentorshipRepository.save(mentorship));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMentorship(@PathVariable int id) {
        if (!mentorshipRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        mentorshipRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

