package com.bad.batch.repository;

import com.bad.batch.model.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoomIdOrderByTimestampDesc(Long chatRoomId);
    List<ChatMessage> findBySenderIdOrderByTimestampDesc(Long senderId);
}
