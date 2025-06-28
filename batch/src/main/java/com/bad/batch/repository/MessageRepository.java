package com.bad.batch.repository;

import com.bad.batch.model.entities.Message;
import com.bad.batch.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Mensajes directos entre dos usuarios
    @Query("""
        SELECT m FROM Message m 
        WHERE m.conversationType = 'DIRECT' 
        AND m.isDeleted = false
        AND ((m.sender.id = :userId1 AND m.recipient.id = :userId2) 
             OR (m.sender.id = :userId2 AND m.recipient.id = :userId1))
        ORDER BY m.createdAt ASC
        """)
    List<Message> findDirectMessagesBetweenUsers(
        @Param("userId1") Long userId1, 
        @Param("userId2") Long userId2
    );

    // Mensajes directos con paginación
    @Query("""
        SELECT m FROM Message m 
        WHERE m.conversationType = 'DIRECT' 
        AND m.isDeleted = false
        AND ((m.sender.id = :userId1 AND m.recipient.id = :userId2) 
             OR (m.sender.id = :userId2 AND m.recipient.id = :userId1))
        """)
    Page<Message> findDirectMessagesBetweenUsers(
        @Param("userId1") Long userId1, 
        @Param("userId2") Long userId2, 
        Pageable pageable
    );

    // Mensajes de un challenge específico
    @Query("""
        SELECT m FROM Message m 
        WHERE m.conversationType = 'CHALLENGE' 
        AND m.challengeId = :challengeId 
        AND m.isDeleted = false
        ORDER BY m.createdAt ASC
        """)
    List<Message> findChallengeMessages(@Param("challengeId") Long challengeId);

    // Mensajes de challenge con paginación
    @Query("""
        SELECT m FROM Message m 
        WHERE m.conversationType = 'CHALLENGE' 
        AND m.challengeId = :challengeId 
        AND m.isDeleted = false
        """)
    Page<Message> findChallengeMessages(
        @Param("challengeId") Long challengeId, 
        Pageable pageable
    );

    // Mensajes de una mentoría específica
    @Query("""
        SELECT m FROM Message m 
        WHERE m.conversationType = 'MENTORSHIP' 
        AND m.mentorshipId = :mentorshipId 
        AND m.isDeleted = false
        ORDER BY m.createdAt ASC
        """)
    List<Message> findMentorshipMessages(@Param("mentorshipId") Long mentorshipId);

    // Mensajes de mentoría con paginación
    @Query("""
        SELECT m FROM Message m 
        WHERE m.conversationType = 'MENTORSHIP' 
        AND m.mentorshipId = :mentorshipId 
        AND m.isDeleted = false
        """)
    Page<Message> findMentorshipMessages(
        @Param("mentorshipId") Long mentorshipId, 
        Pageable pageable
    );

    // Últimos mensajes de cada conversación para un usuario
    @Query("""
        SELECT m FROM Message m 
        WHERE m.id IN (
            SELECT MAX(m2.id) FROM Message m2 
            WHERE m2.conversationType = 'DIRECT' 
            AND m2.isDeleted = false
            AND (m2.sender.id = :userId OR m2.recipient.id = :userId)
            GROUP BY CASE 
                WHEN m2.sender.id = :userId THEN m2.recipient.id 
                ELSE m2.sender.id 
            END
        )
        ORDER BY m.createdAt DESC
        """)
    List<Message> findLastDirectMessagesForUser(@Param("userId") Long userId);

    // Contar mensajes no leídos para un usuario
    @Query("""
        SELECT COUNT(m) FROM Message m 
        WHERE m.recipient.id = :userId 
        AND m.isRead = false 
        AND m.isDeleted = false
        """)
    Long countUnreadMessagesForUser(@Param("userId") Long userId);

    // Contar mensajes no leídos en conversación directa
    @Query("""
        SELECT COUNT(m) FROM Message m 
        WHERE m.recipient.id = :recipientId 
        AND m.sender.id = :senderId
        AND m.isRead = false 
        AND m.isDeleted = false
        """)
    Long countUnreadDirectMessages(
        @Param("recipientId") Long recipientId, 
        @Param("senderId") Long senderId
    );

    // Buscar mensajes por contenido
    @Query("""
        SELECT m FROM Message m 
        WHERE m.isDeleted = false
        AND LOWER(m.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
        AND (
            (m.conversationType = 'DIRECT' 
             AND (m.sender.id = :userId OR m.recipient.id = :userId))
            OR (m.conversationType = 'CHALLENGE' AND m.challengeId = :challengeId)
            OR (m.conversationType = 'MENTORSHIP' AND m.mentorshipId = :mentorshipId)
        )
        """)
    Page<Message> searchMessages(
        @Param("searchTerm") String searchTerm,
        @Param("userId") Long userId,
        @Param("challengeId") Long challengeId,
        @Param("mentorshipId") Long mentorshipId,
        Pageable pageable
    );

    // Mensajes después de una fecha específica (para sincronización)
    @Query("""
        SELECT m FROM Message m 
        WHERE m.conversationType = :conversationType
        AND m.isDeleted = false
        AND m.createdAt > :afterDate
        AND (
            (:challengeId IS NULL OR m.challengeId = :challengeId)
            AND (:mentorshipId IS NULL OR m.mentorshipId = :mentorshipId)
            AND (:userId IS NULL OR m.sender.id = :userId OR m.recipient.id = :userId)
        )
        ORDER BY m.createdAt ASC
        """)
    List<Message> findMessagesAfterDate(
        @Param("conversationType") Message.ConversationType conversationType,
        @Param("afterDate") LocalDateTime afterDate,
        @Param("challengeId") Long challengeId,
        @Param("mentorshipId") Long mentorshipId,
        @Param("userId") Long userId
    );

    // Métodos adicionales necesarios para MessageHistoryService
    
    // Mensajes de challenge con ordenación específica
    Page<Message> findByChallengeIdOrderByCreatedAtAsc(Long challengeId, Pageable pageable);
    
    // Mensajes de mentoría con ordenación específica  
    Page<Message> findByMentorshipIdOrderByCreatedAtAsc(Long mentorshipId, Pageable pageable);
    
    // Últimas conversaciones del usuario
    @Query("""
        SELECT m FROM Message m 
        WHERE m.id IN (
            SELECT MAX(m2.id) FROM Message m2 
            WHERE m2.conversationType = 'DIRECT' 
            AND m2.isDeleted = false
            AND (m2.sender.id = :userId OR m2.recipient.id = :userId)
            GROUP BY CASE 
                WHEN m2.sender.id = :userId THEN m2.recipient.id 
                ELSE m2.sender.id 
            END
        )
        ORDER BY m.createdAt DESC
        """)
    List<Message> findLastConversationMessages(@Param("userId") Long userId);
    
    // Contar mensajes no leídos
    @Query("""
        SELECT COUNT(m) FROM Message m 
        WHERE m.recipient.id = :userId 
        AND m.isRead = false 
        AND m.isDeleted = false
        """)
    Long countUnreadMessages(@Param("userId") Long userId);
    
    // Marcar conversación como leída
    @Modifying
    @Transactional
    @Query("""
        UPDATE Message m 
        SET m.isRead = true, m.readAt = CURRENT_TIMESTAMP 
        WHERE m.recipient.id = :currentUserId 
        AND m.sender.id = :otherUserId 
        AND m.isRead = false 
        AND m.isDeleted = false
        """)
    int markConversationAsRead(@Param("currentUserId") Long currentUserId, @Param("otherUserId") Long otherUserId);
    
    // Buscar en challenge específico
    @Query("""
        SELECT m FROM Message m 
        WHERE m.conversationType = 'CHALLENGE' 
        AND m.challengeId = :challengeId 
        AND m.isDeleted = false
        AND LOWER(m.content) LIKE LOWER(CONCAT('%', :query, '%'))
        """)
    Page<Message> searchInChallenge(@Param("query") String query, @Param("challengeId") Long challengeId, Pageable pageable);
    
    // Buscar en mentoría específica
    @Query("""
        SELECT m FROM Message m 
        WHERE m.conversationType = 'MENTORSHIP' 
        AND m.mentorshipId = :mentorshipId 
        AND m.isDeleted = false
        AND LOWER(m.content) LIKE LOWER(CONCAT('%', :query, '%'))
        """)
    Page<Message> searchInMentorship(@Param("query") String query, @Param("mentorshipId") Long mentorshipId, Pageable pageable);
    
    // Buscar en mensajes del usuario
    @Query("""
        SELECT m FROM Message m 
        WHERE m.isDeleted = false
        AND LOWER(m.content) LIKE LOWER(CONCAT('%', :query, '%'))
        AND (
            (m.conversationType = 'DIRECT' 
             AND (m.sender.id = :userId OR m.recipient.id = :userId))
        )
        """)
    Page<Message> searchUserMessages(@Param("query") String query, @Param("userId") Long userId, Pageable pageable);
}
