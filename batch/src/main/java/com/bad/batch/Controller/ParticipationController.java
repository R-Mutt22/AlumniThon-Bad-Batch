package com.bad.batch.Controller;

import com.bad.batch.Model.Participation;
import com.bad.batch.Enum.ParticipationStatus;
import com.bad.batch.Repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/participations")
public class ParticipationController {
    @Autowired
    private ParticipationRepository participationRepository;

    @GetMapping
    public List<Participation> getAllParticipations() {
        return participationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participation> getParticipationById(@PathVariable Long id) {
        Optional<Participation> participation = participationRepository.findById(id);
        return participation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Participation createParticipation(@RequestBody Participation participation) {
        if (participation.getStatus() == null) {
            participation.setStatus(ParticipationStatus.JOINED);
        }
        return participationRepository.save(participation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participation> updateParticipation(@PathVariable Long id, @RequestBody Participation details) {
        Optional<Participation> optional = participationRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Participation participation = optional.get();
        participation.setStatus(details.getStatus());
        participation.setJoinedAt(details.getJoinedAt());
        participation.setCompletedAt(details.getCompletedAt());
        participation.setProgressPercentage(details.getProgressPercentage());
        participation.setNotes(details.getNotes());
        return ResponseEntity.ok(participationRepository.save(participation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipation(@PathVariable Long id) {
        if (!participationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        participationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

