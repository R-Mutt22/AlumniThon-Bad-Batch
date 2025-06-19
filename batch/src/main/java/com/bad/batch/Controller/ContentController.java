package com.bad.batch.Controller;

import com.bad.batch.Model.Content;
import com.bad.batch.Enum.ContentStatus;
import com.bad.batch.Repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contents")
public class ContentController {
    @Autowired
    private ContentRepository contentRepository;

    @GetMapping
    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable int id) {
        Optional<Content> content = contentRepository.findById(id);
        return content.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Content createContent(@RequestBody Content content) {
        // Validación simple de ContentStatus
        if (content.getStatus() == null) {
            content.setStatus(ContentStatus.DRAFT);
        }
        return contentRepository.save(content);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Content> updateContent(@PathVariable int id, @RequestBody Content contentDetails) {
        Optional<Content> optionalContent = contentRepository.findById(id);
        if (optionalContent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Content content = optionalContent.get();
        content.setTitle(contentDetails.getTitle());
        content.setDescription(contentDetails.getDescription());
        content.setStatus(contentDetails.getStatus());
        content.setDifficulty(contentDetails.getDifficulty());
        content.setRequiredTechnologies(contentDetails.getRequiredTechnologies());
        content.setMaxParticipants(contentDetails.getMaxParticipants());
        content.setStartDate(contentDetails.getStartDate());
        content.setEndDate(contentDetails.getEndDate());
        return ResponseEntity.ok(contentRepository.save(content));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable int id) {
        if (!contentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        contentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

