package com.bad.batch.repository;

import com.bad.batch.model.entities.Participation;
import com.bad.batch.model.entities.Content;
import com.bad.batch.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findByContentAndParticipant(Content content, User participant);
    List<Participation> findByContent(Content content);
    List<Participation> findByParticipant(User participant);
}

