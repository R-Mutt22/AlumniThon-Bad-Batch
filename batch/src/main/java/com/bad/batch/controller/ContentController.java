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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        try {
            // Extraer el creatorId del JWT si no está presente
            if (request.getCreatorId() == null) {
                Long userId = extractUserIdFromJWT(httpRequest);
                request.setCreatorId(userId);
            }
            Content content = contentService.createContentFromRequest(request);
            return ResponseEntity.ok(contentService.toResponse(content));
        } catch (Exception e) {
            System.err.println("Error al crear contenido: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping
    @Operation(summary = "Listar contenidos",
            description = "Devuelve contenidos publicados o filtrados por el ID de un usuario (creador o participante).")
    public ResponseEntity<List<ContentResponse>> getAllContents(@RequestParam(required = false) Long userId) {
        try {
            List<Content> contents = (userId != null)
                    ? contentService.getAllContentsForUser(userId)
                    : contentService.getAllContents().stream()
                    .filter(c -> c.getStatus() != null && c.getStatus().name().equals("PUBLISHED"))
                    .toList();
            return ResponseEntity.ok(contents.stream().map(contentService::toResponse).toList());
        } catch (Exception e) {
            System.err.println("Error al listar contenidos: " + e.getMessage());
            e.printStackTrace();
            // En lugar de propagar la excepción, devolvemos una lista vacía
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener contenido por ID",
            description = "Obtiene los detalles completos de un contenido usando su ID único.")
    public ResponseEntity<ContentResponse> getContentById(@PathVariable Long id) {
        try {
            return contentService.getContentById(id)
                    .map(contentService::toResponse)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            System.err.println("Error al obtener contenido por ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar contenido",
            description = "Modifica los campos de un contenido existente. Requiere ID y cuerpo con datos actualizados.")
    public ResponseEntity<ContentResponse> updateContent(@PathVariable Long id,
                                                         @Valid @RequestBody ContentRequest request) {
        try {
            Content updated = contentService.updateContentFromRequest(id, request);
            return ResponseEntity.ok(contentService.toResponse(updated));
        } catch (Exception e) {
            System.err.println("Error al actualizar contenido ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar contenido",
            description = "Elimina un contenido permanentemente utilizando su ID.")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        try {
            contentService.deleteContent(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Error al eliminar contenido ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/{id}/publish")
    @Operation(summary = "Publicar contenido",
            description = "Marca el contenido como PUBLISHED. Solo el creador puede hacerlo.")
    public ResponseEntity<ContentResponse> publishContent(@PathVariable Long id, @RequestParam Long userId) {
        try {
            Content published = contentService.publishContent(id, userId);
            return ResponseEntity.ok(contentService.toResponse(published));
        } catch (Exception e) {
            System.err.println("Error al publicar contenido ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/{id}/join")
    @Operation(summary = "Unirse a contenido",
            description = "Permite a un usuario participar en una mentoría o desafío.")
    public ResponseEntity<Void> joinContent(@PathVariable Long id, @RequestParam Long userId) {
        try {
            contentService.joinContent(id, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error al unirse al contenido ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            // No lanzamos la excepción para que no falle el endpoint
            return ResponseEntity.ok().build();
        }
    }

    @DeleteMapping("/{id}/leave")
    @Operation(summary = "Salir de contenido",
            description = "Permite a un usuario dejar de participar en un contenido.")
    public ResponseEntity<Void> leaveContent(@PathVariable Long id, @RequestParam Long userId) {
        try {
            contentService.leaveContent(id, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error al salir del contenido ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping("/{id}/participants")
    @Operation(summary = "Listar participantes",
            description = "Devuelve los IDs de los usuarios que están participando en un contenido.")
    public ResponseEntity<List<Long>> getParticipants(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(contentService.getParticipants(id));
        } catch (Exception e) {
            System.err.println("Error al obtener participantes del contenido ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PostMapping("/{id}/submissions")
    @Operation(summary = "Enviar desafío",
            description = "Envía una solución a un contenido de tipo CHALLENGE. El usuario debe estar unido al contenido.")
    public ResponseEntity<ChallengeSubmissionResponse> submitChallenge(@PathVariable Long id,
                                                                       @RequestParam Long userId,
                                                                       @RequestBody ChallengeSubmissionRequest request) {
        try {
            return ResponseEntity.ok(contentService.submitChallenge(id, userId, request));
        } catch (Exception e) {
            System.err.println("Error al enviar submission para el contenido ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/{id}/submissions")
    @Operation(summary = "Listar entregas por usuario",
            description = "Devuelve todas las entregas hechas por un usuario en un desafío específico.")
    public ResponseEntity<List<ChallengeSubmissionResponse>> getChallengeSubmissions(@PathVariable Long id,
                                                                                     @RequestParam Long userId) {
        try {
            return ResponseEntity.ok(contentService.getChallengeSubmissions(id, userId));
        } catch (Exception e) {
            System.err.println("Error al obtener submissions del contenido ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @PutMapping("/{id}/start")
    @Operation(summary = "Iniciar contenido",
            description = "Inicia formalmente el contenido, cambiando su estado a ACTIVE. Solo el creador puede hacerlo.")
    public ResponseEntity<Void> startContent(@PathVariable Long id) {
        try {
            contentService.startContent(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error al iniciar contenido ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping("/{contentId}/participants/{userId}")
    @Operation(summary = "Eliminar participante",
            description = "Permite al creador eliminar un participante específico del contenido.")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long contentId,
                                                  @PathVariable Long userId,
                                                  @RequestParam Long creatorId) {
        try {
            contentService.removeParticipant(contentId, userId, creatorId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Error al eliminar participante " + userId + " del contenido ID " + contentId + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar contenidos",
            description = "Filtra contenidos por tipo, tecnología, dificultad y creador. Admite paginación.")
    public ResponseEntity<List<ContentResponse>> searchContents(
            @RequestParam(required = false) ContentType type,
            @RequestParam(required = false) String tech,
            @RequestParam(required = false) String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId) {
        try {
            List<Content> contents = contentService.searchContents(type, tech, difficulty, page, size, userId);
            List<ContentResponse> responses = contents.stream().map(contentService::toResponse).toList();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            System.err.println("Error al buscar contenidos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    /**
     * Extrae el ID del usuario del token JWT.
     * Este es un método dummy que deberá ser implementado con la lógica real.
     */
    private Long extractUserIdFromJWT(HttpServletRequest request) {
        // Esta es una implementación dummy - en un entorno real, extraeríamos el ID del JWT
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader != null && !userIdHeader.isEmpty()) {
            try {
                return Long.parseLong(userIdHeader);
            } catch (NumberFormatException e) {
                System.err.println("Error al convertir X-User-Id a Long: " + e.getMessage());
            }
        }

        // Valor predeterminado o de emergencia
        return 1L; // ID de un usuario administrador o sistema
    }
}
