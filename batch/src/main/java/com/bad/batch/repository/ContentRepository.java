package com.bad.batch.repository;

import com.bad.batch.model.entities.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    // Métodos personalizados si se necesitan
}

