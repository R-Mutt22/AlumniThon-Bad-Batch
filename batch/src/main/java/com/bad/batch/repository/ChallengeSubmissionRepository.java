package com.bad.batch.repository;

import com.bad.batch.model.entities.ChallengeSubmission;
import com.bad.batch.model.entities.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeSubmissionRepository extends JpaRepository<ChallengeSubmission, Long> {
    List<ChallengeSubmission> findByParticipation(Participation participation);
}

