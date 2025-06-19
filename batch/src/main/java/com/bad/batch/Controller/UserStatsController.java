package com.bad.batch.Controller;

import com.bad.batch.Model.UserStats;
import com.bad.batch.Repository.UserStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user-stats")
public class UserStatsController {
    @Autowired
    private UserStatsRepository userStatsRepository;

    @GetMapping
    public List<UserStats> getAllUserStats() {
        return userStatsRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStats> getUserStatsById(@PathVariable Long id) {
        Optional<UserStats> stats = userStatsRepository.findById(id);
        return stats.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UserStats createUserStats(@RequestBody UserStats stats) {
        return userStatsRepository.save(stats);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserStats> updateUserStats(@PathVariable Long id, @RequestBody UserStats details) {
        Optional<UserStats> optional = userStatsRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UserStats stats = optional.get();
        stats.setChallengesCompleted(details.getChallengesCompleted());
        stats.setChallengesCreated(details.getChallengesCreated());
        stats.setAverageChallengeScore(details.getAverageChallengeScore());
        stats.setTotalChallengeScore(details.getTotalChallengeScore());
        stats.setMentorshipsAttended(details.getMentorshipsAttended());
        stats.setMentorshipsCreated(details.getMentorshipsCreated());
        stats.setMentorshipsCompleted(details.getMentorshipsCompleted());
        stats.setGlobalRank(details.getGlobalRank());
        stats.setTotalPoints(details.getTotalPoints());
        return ResponseEntity.ok(userStatsRepository.save(stats));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserStats(@PathVariable Long id) {
        if (!userStatsRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userStatsRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

