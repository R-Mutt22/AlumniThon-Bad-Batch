package com.bad.batch.controller;

import com.bad.batch.dto.request.ContentRequest;
import com.bad.batch.dto.response.ContentResponse;
import com.bad.batch.model.entities.Content;
import com.bad.batch.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ContentResponse> createContent(@RequestBody ContentRequest request) {
        Content content = contentService.createContentFromRequest(request);
        return ResponseEntity.ok(contentService.toResponse(content));
    }

    @GetMapping
    public ResponseEntity<List<ContentResponse>> getAllContents() {
        List<Content> contents = contentService.getAllContents();
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
    public ResponseEntity<ContentResponse> updateContent(@PathVariable Long id, @RequestBody ContentRequest request) {
        Content updated = contentService.updateContentFromRequest(id, request);
        return ResponseEntity.ok(contentService.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.noContent().build();
    }
}

