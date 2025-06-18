package com.bad.batch.Controller;

import com.bad.batch.Model.ChallengeSubmission;
import com.bad.batch.Enum.SubmissionStatus;
import com.bad.batch.Repository.ChallengeSubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/challenge-submissions")
public class ChallengeSubmissionController {
    @Autowired
    private ChallengeSubmissionRepository challengeSubmissionRepository;

    @GetMapping
    public List<ChallengeSubmission> getAllSubmissions() {
        return challengeSubmissionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeSubmission> getSubmissionById(@PathVariable Long id) {
        Optional<ChallengeSubmission> submission = challengeSubmissionRepository.findById(id);
        return submission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ChallengeSubmission createSubmission(@RequestBody ChallengeSubmission submission) {
        if (submission.getStatus() == null) {
            submission.setStatus(SubmissionStatus.PENDING);
        }
        return challengeSubmissionRepository.save(submission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChallengeSubmission> updateSubmission(@PathVariable Long id, @RequestBody ChallengeSubmission details) {
        Optional<ChallengeSubmission> optional = challengeSubmissionRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ChallengeSubmission submission = optional.get();
        submission.setRepositoryUrl(details.getRepositoryUrl());
        submission.setDemoUrl(details.getDemoUrl());
        submission.setDescription(details.getDescription());
        submission.setStatus(details.getStatus());
        submission.setSubmittedAt(details.getSubmittedAt());
        submission.setReviewedAt(details.getReviewedAt());
        submission.setScore(details.getScore());
        submission.setFeedback(details.getFeedback());
        submission.setReviewer(details.getReviewer());
        return ResponseEntity.ok(challengeSubmissionRepository.save(submission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        if (!challengeSubmissionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        challengeSubmissionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

