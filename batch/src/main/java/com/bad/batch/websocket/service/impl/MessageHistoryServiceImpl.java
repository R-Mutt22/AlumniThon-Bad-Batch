package com.bad.batch.websocket.service.impl;

import com.bad.batch.model.entities.Message;
import com.bad.batch.repository.MessageRepository;
import com.bad.batch.websocket.dto.ChatMessageResponse;
import com.bad.batch.websocket.service.MessageHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MessageHistoryServiceImpl implements MessageHistoryService {
    
    private final MessageRepository messageRepository;

    @Override
    public Page<ChatMessageResponse> getDirectMessages(Long userId1, Long userId2, Pageable pageable) {
        log.debug("Obteniendo mensajes directos entre {} y {}", userId1, userId2);
        
        // Ordenar por fecha descendente para obtener los más recientes primero
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            sort
        );
        
        Page<Message> messages = messageRepository.findDirectMessagesBetweenUsers(
            userId1, userId2, pageRequest
        );
        
        return messages.map(this::convertToResponse);
    }

    @Override
    public Page<ChatMessageResponse> getChallengeMessages(Long challengeId, Pageable pageable) {
        log.debug("Obteniendo mensajes del challenge {}", challengeId);
        
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt"); // Para challenges, orden cronológico
        PageRequest pageRequest = PageRequest.of(
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            sort
        );
        
        Page<Message> messages = messageRepository.findByChallengeIdOrderByCreatedAtAsc(
            challengeId, pageRequest
        );
        
        return messages.map(this::convertToResponse);
    }

    @Override
    public Page<ChatMessageResponse> getMentorshipMessages(Long mentorshipId, Pageable pageable) {
        log.debug("Obteniendo mensajes de la mentoría {}", mentorshipId);
        
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt"); // Para mentorías, orden cronológico
        PageRequest pageRequest = PageRequest.of(
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            sort
        );
        
        Page<Message> messages = messageRepository.findByMentorshipIdOrderByCreatedAtAsc(
            mentorshipId, pageRequest
        );
        
        return messages.map(this::convertToResponse);
    }

    @Override
    public List<ChatMessageResponse> getLastConversations(Long userId) {
        log.debug("Obteniendo últimas conversaciones para el usuario {}", userId);
        
        // Obtener los últimos mensajes únicos por conversación
        List<Message> lastMessages = messageRepository.findLastConversationMessages(userId);
        
        return lastMessages.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public Long getUnreadCount(Long userId) {
        log.debug("Contando mensajes no leídos para el usuario {}", userId);
        return messageRepository.countUnreadMessages(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long messageId, Long userId) {
        log.debug("Marcando mensaje {} como leído por usuario {}", messageId, userId);
        
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new IllegalArgumentException("Mensaje no encontrado: " + messageId));
        
        // Solo el destinatario puede marcar como leído
        if (message.getRecipient() != null && message.getRecipient().getId().equals(userId)) {
            message.setReadAt(java.time.LocalDateTime.now());
            messageRepository.save(message);
            log.info("Mensaje {} marcado como leído", messageId);
        } else {
            log.warn("Usuario {} no autorizado para marcar mensaje {} como leído", userId, messageId);
        }
    }

    @Override
    @Transactional
    public void markConversationAsRead(Long userId, Long otherUserId) {
        log.debug("Marcando conversación entre {} y {} como leída", userId, otherUserId);
        
        int updatedMessages = messageRepository.markConversationAsRead(userId, otherUserId);
        log.info("Marcados {} mensajes como leídos en la conversación", updatedMessages);
    }

    @Override
    public Page<ChatMessageResponse> searchMessages(String query, Long userId, Long challengeId, Long mentorshipId, Pageable pageable) {
        log.debug("Buscando mensajes con query '{}' para usuario {}", query, userId);
        
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            sort
        );
        
        Page<Message> messages;
        
        if (challengeId != null) {
            // Buscar solo en el challenge específico
            messages = messageRepository.searchInChallenge(query, challengeId, pageRequest);
        } else if (mentorshipId != null) {
            // Buscar solo en la mentoría específica
            messages = messageRepository.searchInMentorship(query, mentorshipId, pageRequest);
        } else {
            // Buscar en todas las conversaciones del usuario
            messages = messageRepository.searchUserMessages(query, userId, pageRequest);
        }
        
        return messages.map(this::convertToResponse);
    }

    /**
     * Convierte una entidad Message a ChatMessageResponse
     */
    private ChatMessageResponse convertToResponse(Message message) {
        ChatMessageResponse.ChatMessageResponseBuilder builder = ChatMessageResponse.builder()
            .id(message.getId())
            .content(message.getContent())
            .type(message.getType())
            .senderId(message.getSender().getId())
            .senderName(message.getSender().getFirstName() + " " + message.getSender().getLastName())
            .timestamp(message.getCreatedAt())
            .isSystem(message.getIsSystem())
            .readAt(message.getReadAt());

        // Agregar destinatario si existe
        if (message.getRecipient() != null) {
            builder.recipientId(message.getRecipient().getId());
        }

        // Agregar IDs de challenge o mentoría si existen
        if (message.getChallengeId() != null) {
            builder.challengeId(message.getChallengeId());
        }

        if (message.getMentorshipId() != null) {
            builder.mentorshipId(message.getMentorshipId());
        }

        return builder.build();
    }
}
