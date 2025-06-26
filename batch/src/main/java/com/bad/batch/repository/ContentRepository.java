package com.bad.batch.repository;

import com.bad.batch.model.entities.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    
    // Método para obtener contenido con tecnologías cargadas eagerly
    @Query("SELECT c FROM Content c " +
           "JOIN FETCH c.creator u " +
           "LEFT JOIN FETCH u.profile " +
           "LEFT JOIN FETCH c.requiredTechnologies " +
           "WHERE c.id = :id")
    Optional<Content> findByIdWithTechnologies(@Param("id") Long id);
    
    // Método para obtener todos los contenidos con tecnologías cargadas eagerly
    @Query("SELECT DISTINCT c FROM Content c " +
           "JOIN FETCH c.creator u " +
           "LEFT JOIN FETCH u.profile " +
           "LEFT JOIN FETCH c.requiredTechnologies " +
           "ORDER BY c.createdAt DESC")
    List<Content> findAllWithTechnologies();
    
    // Método para obtener contenidos con paginación y filtros, incluyendo tecnologías
    @Query("SELECT DISTINCT c FROM Content c " +
           "JOIN FETCH c.creator u " +
           "LEFT JOIN FETCH u.profile " +
           "LEFT JOIN FETCH c.requiredTechnologies " +
           "WHERE (:userId IS NULL OR u.id = :userId) " +
           "ORDER BY c.createdAt DESC")
    List<Content> findAllWithTechnologiesForSearch(@Param("userId") Long userId);
}
