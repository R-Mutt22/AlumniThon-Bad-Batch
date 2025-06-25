package com.bad.batch.repository;

import com.bad.batch.model.entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByContentId(Long contentId);
    Optional<ChatRoom> findByRoomId(String roomId);
}
