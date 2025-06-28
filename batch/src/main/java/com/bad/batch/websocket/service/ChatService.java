package com.bad.batch.websocket.service;

import com.bad.batch.websocket.dto.ChatMessageRequest;
import com.bad.batch.websocket.dto.ChatMessageResponse;

public interface ChatService {
    ChatMessageResponse sendDirectMessage(Long senderId, ChatMessageRequest request);
    ChatMessageResponse sendChallengeMessage(Long senderId, ChatMessageRequest request);
    ChatMessageResponse sendMentorshipMessage(Long senderId, ChatMessageRequest request);
    void notifyUserJoined(Long userId, String destination);
    void notifyUserLeft(Long userId, String destination);
}
