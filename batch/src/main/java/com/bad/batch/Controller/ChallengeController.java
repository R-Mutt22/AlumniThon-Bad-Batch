package com.bad.batch.Controller;

import com.bad.batch.Model.Challenge;
import com.bad.batch.Enum.ChallengeType;
import com.bad.batch.Repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/challenges")
public class ChallengeController {
    @Autowired
    private ChallengeRepository challengeRepository;

    @GetMapping
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Challenge> getChallengeById(@PathVariable int id) {
        Optional<Challenge> challenge = challengeRepository.findById(id);
        return challenge.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Challenge createChallenge(@RequestBody Challenge challenge) {
        // Validación simple de ChallengeType
        if (challenge.getType() == null) {
            challenge.setType(ChallengeType.TECHNICAL);
        }
        return challengeRepository.save(challenge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Challenge> updateChallenge(@PathVariable int id, @RequestBody Challenge challengeDetails) {
        Optional<Challenge> optionalChallenge = challengeRepository.findById(id);
        if (optionalChallenge.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Challenge challenge = optionalChallenge.get();
        challenge.setProblemStatement(challengeDetails.getProblemStatement());
        challenge.setAcceptanceCriteria(challengeDetails.getAcceptanceCriteria());
        challenge.setRepositoryTemplate(challengeDetails.getRepositoryTemplate());
        challenge.setAllowsTeams(challengeDetails.getAllowsTeams());
        challenge.setType(challengeDetails.getType());
        return ResponseEntity.ok(challengeRepository.save(challenge));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable int id) {
        if (!challengeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        challengeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

