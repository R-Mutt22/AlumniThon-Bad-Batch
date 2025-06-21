package com.bad.batch.repository;

import com.bad.batch.model.enums.ExperienceLevel;
import com.bad.batch.model.entities.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    Optional<Profile>findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    // Búsqueda básica por texto en perfiles públicos
    @Query("SELECT p FROM Profile p JOIN p.user u " +
            "WHERE p.visibility = 'PUBLIC' " +
            "AND u.isActive = true " +
            "AND (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.bio) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Profile> findPublicProfilesByTextQuery(@Param("query") String query, Pageable pageable);

    // Búsqueda por nivel de experiencia
    @Query("SELECT p FROM Profile p JOIN p.user u " +
            "WHERE p.visibility = 'PUBLIC' " +
            "AND u.isActive = true " +
            "AND p.experienceLevel = :level")
    Page<Profile> findPublicProfilesByExperienceLevel(@Param("level") ExperienceLevel level, Pageable pageable);

    // Búsqueda por ubicación
    @Query("SELECT p FROM Profile p JOIN p.user u " +
            "WHERE p.visibility = 'PUBLIC' " +
            "AND u.isActive = true " +
            "AND LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    Page<Profile> findPublicProfilesByLocation(@Param("location") String location, Pageable pageable);

    // Búsqueda compleja con múltiples filtros
    @Query("SELECT DISTINCT p FROM Profile p JOIN p.user u " +
            "LEFT JOIN p.technologies t " +
            "LEFT JOIN p.interests i " +
            "WHERE p.visibility = 'PUBLIC' " +
            "AND u.isActive = true " +
            "AND (:query IS NULL OR :query = '' OR " +
            "     LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "     LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "     LOWER(p.bio) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:experienceLevel IS NULL OR p.experienceLevel = :experienceLevel) " +
            "AND (:location IS NULL OR :location = '' OR " +
            "     LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:hasTechnologies = false OR t IN :technologies) " +
            "AND (:hasInterests = false OR i IN :interests)")
    Page<Profile> findPublicProfilesWithFilters(
            @Param("query") String query,
            @Param("experienceLevel") ExperienceLevel experienceLevel,
            @Param("location") String location,
            @Param("technologies") Set<String> technologies,
            @Param("hasTechnologies") boolean hasTechnologies,
            @Param("interests") Set<String> interests,
            @Param("hasInterests") boolean hasInterests,
            Pageable pageable
    );

    // Buscar perfiles por tecnologías específicas
    @Query("SELECT DISTINCT p FROM Profile p JOIN p.user u " +
            "JOIN p.technologies t " +
            "WHERE p.visibility = 'PUBLIC' " +
            "AND u.isActive = true " +
            "AND t IN :technologies")
    Page<Profile> findPublicProfilesByTechnologies(@Param("technologies") Set<String> technologies, Pageable pageable);

    // Buscar perfiles por intereses
    @Query("SELECT DISTINCT p FROM Profile p JOIN p.user u " +
            "JOIN p.interests i " +
            "WHERE p.visibility = 'PUBLIC' " +
            "AND u.isActive = true " +
            "AND i IN :interests")
    Page<Profile> findPublicProfilesByInterests(@Param("interests") Set<String> interests, Pageable pageable);

    // Contar perfiles públicos
    @Query("SELECT COUNT(p) FROM Profile p JOIN p.user u " +
            "WHERE p.visibility = 'PUBLIC' AND u.isActive = true")
    long countPublicProfiles();


}
