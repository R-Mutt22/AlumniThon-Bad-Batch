package com.bad.batch.controller;

import com.bad.batch.model.enums.ContentType;
import com.bad.batch.model.enums.ContentStatus;
import com.bad.batch.dto.request.ChallengeSubmissionRequest;
import com.bad.batch.dto.response.ChallengeSubmissionResponse;
import com.bad.batch.dto.request.ContentRequest;
import com.bad.batch.dto.response.ContentResponse;
import com.bad.batch.model.entities.Content;
import com.bad.batch.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
@Tag(name = "Contenidos", description = "Gestión de mentorías y desafíos técnicos dentro de SkillLink")
public class ContentController {

    private final ContentService contentService;

    @PostMapping
    @Operation(summary = "Crear nuevo contenido",
            description = "Crea un nuevo contenido del tipo MENTORSHIP o CHALLENGE, con detalles como título, tecnología, dificultad y fechas.")
    public ResponseEntity<ContentResponse> createContent(@Valid @RequestBody ContentRequest request, HttpServletRequest httpRequest) {
        // Extraer el creatorId del JWT si no está presente
        if (request.getCreatorId() == null) {
            Long userId = extractUserIdFromJWT(httpRequest);
            request.setCreatorId(userId);
        }
        Content content = contentService.createContentFromRequest(request);
        return ResponseEntity.ok(contentService.toResponse(content));
    }

    @GetMapping
    @Operation(summary = "Listar contenidos",
            description = "Devuelve contenidos publicados o filtrados por el ID de un usuario (creador o participante).")
    public ResponseEntity<List<ContentResponse>> getAllContents(@RequestParam(required = false) Long userId) {
        List<Content> contents = (userId != null)
                ? contentService.getAllContentsForUser(userId)
                : contentService.getAllContents().stream()
                .filter(c -> c.getStatus() != null && c.getStatus().name().equals("PUBLISHED"))
                .toList();
        return ResponseEntity.ok(contents.stream().map(contentService::toResponse).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener contenido por ID",
            description = "Obtiene los detalles completos de un contenido usando su ID único.")
    public ResponseEntity<ContentResponse> getContentById(@PathVariable Long id) {
        return contentService.getContentById(id)
                .map(contentService::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar contenido",
            description = "Modifica los campos de un contenido existente. Requiere ID y cuerpo con datos actualizados.")
    public ResponseEntity<ContentResponse> updateContent(@PathVariable Long id,
                                                         @Valid @RequestBody ContentRequest request) {
        Content updated = contentService.updateContentFromRequest(id, request);
        return ResponseEntity.ok(contentService.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar contenido",
            description = "Elimina un contenido permanentemente utilizando su ID.")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "Publicar contenido",
            description = "Marca el contenido como PUBLISHED. Solo el creador puede hacerlo.")
    public ResponseEntity<ContentResponse> publishContent(@PathVariable Long id, @RequestParam Long userId) {
        Content published = contentService.publishContent(id, userId);
        return ResponseEntity.ok(contentService.toResponse(published));
    }

    @PostMapping("/{id}/join")
    @Operation(summary = "Unirse a contenido",
            description = "Permite a un usuario participar en una mentoría o desafío.")
    public ResponseEntity<Void> joinContent(@PathVariable Long id, @RequestParam Long userId) {
        contentService.joinContent(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/leave")
    @Operation(summary = "Salir de contenido",
            description = "Permite a un usuario dejar de participar en un contenido.")
    public ResponseEntity<Void> leaveContent(@PathVariable Long id, @RequestParam Long userId) {
        contentService.leaveContent(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/participants")
    @Operation(summary = "Listar participantes",
            description = "Devuelve los IDs de los usuarios que están participando en un contenido.")
    public ResponseEntity<List<Long>> getParticipants(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.getParticipants(id));
    }

    @PostMapping("/{id}/submissions")
    @Operation(summary = "Enviar desafío",
            description = "Envía una solución a un contenido de tipo CHALLENGE. El usuario debe estar unido al contenido.")
    public ResponseEntity<ChallengeSubmissionResponse> submitChallenge(@PathVariable Long id,
                                                                       @RequestParam Long userId,
                                                                       @RequestBody ChallengeSubmissionRequest request) {
        return ResponseEntity.ok(contentService.submitChallenge(id, userId, request));
    }

    @GetMapping("/{id}/submissions")
    @Operation(summary = "Listar entregas por usuario",
            description = "Devuelve todas las entregas hechas por un usuario en un desafío específico.")
    public ResponseEntity<List<ChallengeSubmissionResponse>> getChallengeSubmissions(@PathVariable Long id,
                                                                                     @RequestParam Long userId) {
        return ResponseEntity.ok(contentService.getChallengeSubmissions(id, userId));
    }

    @PutMapping("/{id}/start")
    @Operation(summary = "Iniciar contenido",
            description = "Inicia formalmente el contenido, cambiando su estado a ACTIVE. Solo el creador puede hacerlo.")
    public ResponseEntity<Void> startContent(@PathVariable Long id) {
        contentService.startContent(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{contentId}/participants/{userId}")
    @Operation(summary = "Eliminar participante",
            description = "Permite al creador eliminar un participante específico del contenido.")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long contentId,
                                                  @PathVariable Long userId,
                                                  @RequestParam Long creatorId) {
        contentService.removeParticipant(contentId, userId, creatorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar contenidos",
            description = "Filtra contenidos por tipo, tecnología, dificultad y creador. Admite paginación.")
    public ResponseEntity<List<ContentResponse>> searchContents(@RequestParam(required = false) ContentType type,
                                                                @RequestParam(required = false) String tech,
                                                                @RequestParam(required = false) String difficulty,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(required = false) Long userId) {
        List<Content> contents = contentService.searchContents(type, tech, difficulty, page, size, userId);
        return ResponseEntity.ok(contents.stream().map(contentService::toResponse).toList());
    }

    private Long extractUserIdFromJWT(HttpServletRequest request) {
        // Primero intentar desde el atributo establecido por JwtAuthenticationFilter
        Long userId = (Long) request.getAttribute("X-User-Id");

        if (userId == null) {
            // Si no está disponible, intentar extraer del header Authorization
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
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
