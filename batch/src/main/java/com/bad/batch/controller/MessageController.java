package com.bad.batch.controller;

import com.bad.batch.websocket.dto.ChatMessageResponse;
import com.bad.batch.websocket.service.MessageHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Gestión de historial de mensajes")
@SecurityRequirement(name = "JWT")
public class MessageController {
    
    private final MessageHistoryService messageHistoryService;

    @GetMapping("/direct/{otherUserId}")
    @Operation(
        summary = "Obtener historial de mensajes directos",
        description = "Recupera el historial de mensajes entre el usuario autenticado y otro usuario específico."
    )
    public ResponseEntity<Page<ChatMessageResponse>> getDirectMessages(
            @PathVariable Long otherUserId,
            Pageable pageable,
            HttpServletRequest request) {
        
        Long currentUserId = extractUserIdFromRequest(request);
        Page<ChatMessageResponse> messages = messageHistoryService.getDirectMessages(
            currentUserId, otherUserId, pageable
        );
        
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/challenge/{challengeId}")
    @Operation(
        summary = "Obtener historial de mensajes de challenge",
        description = "Recupera el historial de mensajes de un challenge específico."
    )
    public ResponseEntity<Page<ChatMessageResponse>> getChallengeMessages(
            @PathVariable Long challengeId,
            Pageable pageable,
            HttpServletRequest request) {
        
        Page<ChatMessageResponse> messages = messageHistoryService.getChallengeMessages(
            challengeId, pageable
        );
        
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/mentorship/{mentorshipId}")
    @Operation(
        summary = "Obtener historial de mensajes de mentoría",
        description = "Recupera el historial de mensajes de una mentoría específica."
    )
    public ResponseEntity<Page<ChatMessageResponse>> getMentorshipMessages(
            @PathVariable Long mentorshipId,
            Pageable pageable,
            HttpServletRequest request) {
        
        Page<ChatMessageResponse> messages = messageHistoryService.getMentorshipMessages(
            mentorshipId, pageable
        );
        
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/conversations")
    @Operation(
        summary = "Obtener últimas conversaciones",
        description = "Recupera las últimas conversaciones del usuario autenticado."
    )
    public ResponseEntity<List<ChatMessageResponse>> getLastConversations(
            HttpServletRequest request) {
        
        Long currentUserId = extractUserIdFromRequest(request);
        List<ChatMessageResponse> conversations = messageHistoryService.getLastConversations(currentUserId);
        
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/unread/count")
    @Operation(
        summary = "Contar mensajes no leídos",
        description = "Obtiene el número total de mensajes no leídos del usuario."
    )
    public ResponseEntity<Long> getUnreadCount(
            HttpServletRequest request) {
        
        Long currentUserId = extractUserIdFromRequest(request);
        Long unreadCount = messageHistoryService.getUnreadCount(currentUserId);
        
        return ResponseEntity.ok(unreadCount);
    }

    @PutMapping("/{messageId}/read")
    @Operation(
        summary = "Marcar mensaje como leído",
        description = "Marca un mensaje específico como leído."
    )
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long messageId,
            HttpServletRequest request) {
        
        Long currentUserId = extractUserIdFromRequest(request);
        messageHistoryService.markAsRead(messageId, currentUserId);
        
        return ResponseEntity.ok().build();
    }

    @PutMapping("/conversation/{otherUserId}/read")
    @Operation(
        summary = "Marcar conversación como leída",
        description = "Marca todos los mensajes de una conversación como leídos."
    )
    public ResponseEntity<Void> markConversationAsRead(
            @PathVariable Long otherUserId,
            HttpServletRequest request) {
        
        Long currentUserId = extractUserIdFromRequest(request);
        messageHistoryService.markConversationAsRead(currentUserId, otherUserId);
        
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    @Operation(
        summary = "Buscar mensajes",
        description = "Busca mensajes por contenido en las conversaciones del usuario."
    )
    public ResponseEntity<Page<ChatMessageResponse>> searchMessages(
            @RequestParam String query,
            @RequestParam(required = false) Long challengeId,
            @RequestParam(required = false) Long mentorshipId,
            Pageable pageable,
            HttpServletRequest request) {
        
        Long currentUserId = extractUserIdFromRequest(request);
        Page<ChatMessageResponse> messages = messageHistoryService.searchMessages(
            query, currentUserId, challengeId, mentorshipId, pageable
        );
        
        return ResponseEntity.ok(messages);
    }

    private Long extractUserIdFromRequest(HttpServletRequest request) {
        // Extraer el ID del usuario desde el atributo establecido por JwtAuthenticationFilter
        Long userId = (Long) request.getAttribute("X-User-Id");
        if (userId == null) {
            throw new IllegalArgumentException("No se pudo obtener el ID del usuario autenticado");
        }
        return userId;
    }
}
