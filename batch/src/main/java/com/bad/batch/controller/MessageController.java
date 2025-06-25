package com.bad.batch.controller;

import com.bad.batch.websocket.dto.ChatMessageRequest;
import com.bad.batch.websocket.dto.ChatMessageResponse;
import com.bad.batch.websocket.service.ChatService;
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

import com.bad.batch.model.entities.ChatRoom;
import com.bad.batch.model.entities.ChatMessage;
import com.bad.batch.model.entities.Content;
import com.bad.batch.model.entities.User;
import com.bad.batch.model.enums.ChatMessageType;
import com.bad.batch.repository.ChatRoomRepository;
import com.bad.batch.repository.ChatMessageRepository;
import com.bad.batch.repository.ContentRepository;
import com.bad.batch.repository.UserRepository;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Gestión de historial de mensajes")
@SecurityRequirement(name = "JWT")
public class MessageController {
    
    private final MessageHistoryService messageHistoryService;
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

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

    @PostMapping("/test/send")
    @Operation(
        summary = "Enviar mensaje (Test)",
        description = "Envía un mensaje de prueba. Este endpoint es solo para propósitos de testing."
    )
    public ResponseEntity<ChatMessageResponse> sendTestMessage(
            @RequestBody ChatMessageRequest messageRequest,
            HttpServletRequest request) {
        
        Long currentUserId = extractUserIdFromRequest(request);
        ChatMessageResponse response;
        
        // Determinar qué tipo de mensaje enviar basado en los campos del request
        if (messageRequest.getChallengeId() != null) {
            response = chatService.sendChallengeMessage(currentUserId, messageRequest);
        } else if (messageRequest.getMentorshipId() != null) {
            response = chatService.sendMentorshipMessage(currentUserId, messageRequest);
        } else {
            response = chatService.sendDirectMessage(currentUserId, messageRequest);
        }
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/test/create-chat-room")
    @Operation(
        summary = "Crear sala de chat (Test)",
        description = "Crea una sala de chat y mensajes de prueba para un contenido específico."
    )
    public ResponseEntity<String> createChatRoomTest(
            @RequestParam Long contentId,
            HttpServletRequest request) {
        
        Long currentUserId = extractUserIdFromRequest(request);
        
        // 1. Buscar el contenido
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Contenido no encontrado"));
        
        // 2. Crear o buscar ChatRoom para este contenido
        ChatRoom chatRoom = chatRoomRepository.findByContentId(contentId)
                .orElseGet(() -> {
                    ChatRoom newRoom = new ChatRoom();
                    newRoom.setContent(content);
                    newRoom.setRoomId("room_" + contentId + "_" + System.currentTimeMillis());
                    newRoom.setIsActive(true);
                    return chatRoomRepository.save(newRoom);
                });
        
        // 3. Buscar el usuario
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // 4. Crear algunos mensajes de prueba
        ChatMessage message1 = new ChatMessage();
        message1.setChatRoom(chatRoom);
        message1.setSender(user);
        message1.setMessage("¡Hola! Este es el primer mensaje en la sala de chat para: " + content.getTitle());
        message1.setType(ChatMessageType.TEXT);
        message1.setIsSystemMessage(false);
        chatMessageRepository.save(message1);
        
        ChatMessage message2 = new ChatMessage();
        message2.setChatRoom(chatRoom);
        message2.setSender(user);
        message2.setMessage("¿Alguien más está participando en este " + content.getType().toString().toLowerCase() + "?");
        message2.setType(ChatMessageType.TEXT);
        message2.setIsSystemMessage(false);
        chatMessageRepository.save(message2);
        
        ChatMessage systemMessage = new ChatMessage();
        systemMessage.setChatRoom(chatRoom);
        systemMessage.setSender(user);
        systemMessage.setMessage("Sala de chat creada para: " + content.getTitle());
        systemMessage.setType(ChatMessageType.SYSTEM);
        systemMessage.setIsSystemMessage(true);
        chatMessageRepository.save(systemMessage);
        
        return ResponseEntity.ok("Sala de chat creada exitosamente. ID: " + chatRoom.getId() + 
                               ", Room ID: " + chatRoom.getRoomId() + 
                               ", Mensajes creados: 3");
    }

    private Long extractUserIdFromRequest(HttpServletRequest request) {
        // Primero intentar desde el atributo establecido por JwtAuthenticationFilter
        Long userId = (Long) request.getAttribute("X-User-Id");
        
        if (userId == null) {
            // Si no está disponible, intentar extraer del header Authorization
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                try {
                    // Para propósitos de prueba, usar un ID fijo
                    // En producción se debe decodificar el JWT correctamente
                    userId = 5L; // Usuario de prueba
                } catch (Exception e) {
                    throw new IllegalArgumentException("Token JWT inválido");
                }
            }
        }
        
        if (userId == null) {
            throw new IllegalArgumentException("No se pudo obtener el ID del usuario autenticado");
        }
        return userId;
    }
}
