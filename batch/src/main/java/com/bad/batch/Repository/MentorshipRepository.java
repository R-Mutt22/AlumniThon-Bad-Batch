package com.bad.batch.Repository;

import com.bad.batch.Model.Mentorship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorshipRepository extends JpaRepository<Mentorship, Integer> {
}

