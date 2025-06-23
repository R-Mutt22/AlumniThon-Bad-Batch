package com.bad.batch.websocket.service;

import com.bad.batch.websocket.dto.ChatMessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Servicio para gestionar el historial de mensajes
 */
public interface MessageHistoryService {
    
    /**
     * Obtiene el historial de mensajes directos entre dos usuarios
     * @param userId1 ID del primer usuario
     * @param userId2 ID del segundo usuario
     * @param pageable Información de paginación
     * @return Página de mensajes
     */
    Page<ChatMessageResponse> getDirectMessages(Long userId1, Long userId2, Pageable pageable);
    
    /**
     * Obtiene el historial de mensajes de un challenge
     * @param challengeId ID del challenge
     * @param pageable Información de paginación
     * @return Página de mensajes
     */
    Page<ChatMessageResponse> getChallengeMessages(Long challengeId, Pageable pageable);
    
    /**
     * Obtiene el historial de mensajes de una mentoría
     * @param mentorshipId ID de la mentoría
     * @param pageable Información de paginación
     * @return Página de mensajes
     */
    Page<ChatMessageResponse> getMentorshipMessages(Long mentorshipId, Pageable pageable);
    
    /**
     * Obtiene las últimas conversaciones del usuario
     * @param userId ID del usuario
     * @return Lista de últimos mensajes de cada conversación
     */
    List<ChatMessageResponse> getLastConversations(Long userId);
    
    /**
     * Cuenta los mensajes no leídos del usuario
     * @param userId ID del usuario
     * @return Número de mensajes no leídos
     */
    Long getUnreadCount(Long userId);
    
    /**
     * Marca un mensaje como leído
     * @param messageId ID del mensaje
     * @param userId ID del usuario que lee el mensaje
     */
    void markAsRead(Long messageId, Long userId);
    
    /**
     * Marca toda una conversación como leída
     * @param userId ID del usuario actual
     * @param otherUserId ID del otro usuario en la conversación
     */
    void markConversationAsRead(Long userId, Long otherUserId);
    
    /**
     * Busca mensajes por contenido
     * @param query Texto a buscar
     * @param userId ID del usuario actual
     * @param challengeId ID del challenge (opcional)
     * @param mentorshipId ID de la mentoría (opcional)
     * @param pageable Información de paginación
     * @return Página de mensajes que coinciden con la búsqueda
     */
    Page<ChatMessageResponse> searchMessages(String query, Long userId, Long challengeId, Long mentorshipId, Pageable pageable);
}
