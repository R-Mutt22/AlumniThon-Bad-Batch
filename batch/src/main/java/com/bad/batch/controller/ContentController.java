package com.bad.batch.controller;

import com.bad.batch.model.enums.ContentType;
import com.bad.batch.dto.request.ChallengeSubmissionRequest;
import com.bad.batch.dto.response.ChallengeSubmissionResponse;
import com.bad.batch.dto.request.ContentRequest;
import com.bad.batch.dto.response.ContentResponse;
import com.bad.batch.model.entities.Content;
import com.bad.batch.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contents")
public class ContentController {
    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping
    public ResponseEntity<ContentResponse> createContent(@Valid @RequestBody ContentRequest request) {
        Content content = contentService.createContentFromRequest(request);
        return ResponseEntity.ok(contentService.toResponse(content));
    }

    @GetMapping
    public ResponseEntity<List<ContentResponse>> getAllContents(@RequestParam(required = false) Long userId) {
        List<Content> contents;
        if (userId != null) {
            contents = contentService.getAllContentsForUser(userId);
        } else {
            contents = contentService.getAllContents().stream()
                .filter(c -> c.getStatus() != null && c.getStatus().name().equals("PUBLISHED"))
                .toList();
        }
        List<ContentResponse> responses = contents.stream().map(contentService::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentResponse> getContentById(@PathVariable Long id) {
        return contentService.getContentById(id)
                .map(contentService::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentResponse> updateContent(@PathVariable Long id, @Valid @RequestBody ContentRequest request) {
        Content updated = contentService.updateContentFromRequest(id, request);
        return ResponseEntity.ok(contentService.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<ContentResponse> publishContent(@PathVariable Long id, @RequestParam Long userId) {
        Content published = contentService.publishContent(id, userId);
        return ResponseEntity.ok(contentService.toResponse(published));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinContent(@PathVariable Long id, @RequestParam Long userId) {
        contentService.joinContent(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/leave")
    public ResponseEntity<Void> leaveContent(@PathVariable Long id, @RequestParam Long userId) {
        contentService.leaveContent(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<Long>> getParticipants(@PathVariable Long id) {
        List<Long> participants = contentService.getParticipants(id);
        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{id}/submissions")
    public ResponseEntity<ChallengeSubmissionResponse> submitChallenge(@PathVariable Long id, @RequestParam Long userId, @RequestBody ChallengeSubmissionRequest request) {
        ChallengeSubmissionResponse response = contentService.submitChallenge(id, userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/submissions")
    public ResponseEntity<List<ChallengeSubmissionResponse>> getChallengeSubmissions(@PathVariable Long id, @RequestParam Long userId) {
        List<ChallengeSubmissionResponse> submissions = contentService.getChallengeSubmissions(id, userId);
        return ResponseEntity.ok(submissions);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Void> startContent(@PathVariable Long id) {
        contentService.startContent(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{contentId}/participants/{userId}")
    public ResponseEntity<Void> removeParticipant(@PathVariable Long contentId, @PathVariable Long userId, @RequestParam Long creatorId) {
        contentService.removeParticipant(contentId, userId, creatorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContentResponse>> searchContents(
            @RequestParam(required = false) ContentType type,
            @RequestParam(required = false) String tech,
            @RequestParam(required = false) String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long userId) {
        List<Content> contents = contentService.searchContents(type, tech, difficulty, page, size, userId);
        List<ContentResponse> responses = contents.stream().map(contentService::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
// Limpieza: no hay imports ni código innecesario, solo advertencias de métodos no usados (pueden ignorarse en controladores Spring).
// No se requiere cambio en este archivo para warnings actuales.
