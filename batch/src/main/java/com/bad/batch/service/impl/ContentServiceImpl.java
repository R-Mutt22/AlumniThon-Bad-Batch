package com.bad.batch.service.impl;

import com.bad.batch.dto.request.ContentRequest;
import com.bad.batch.dto.response.ContentResponse;
import com.bad.batch.model.entities.Content;
import com.bad.batch.model.entities.User;
import com.bad.batch.model.enums.ContentStatus;
import com.bad.batch.model.enums.DifficultyLevel;
import com.bad.batch.repository.ContentRepository;
import com.bad.batch.repository.UserRepository;
import com.bad.batch.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContentServiceImpl implements ContentService {
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    @Autowired
    public ContentServiceImpl(ContentRepository contentRepository, UserRepository userRepository) {
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Content createContent(Content content) {
        return contentRepository.save(content);
    }

    @Override
    @Transactional
    public Content createContentFromRequest(ContentRequest request) {
        Content content = new Content();
        mapRequestToContent(request, content);
        return contentRepository.save(content);
    }

    @Override
    public List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    @Override
    public Optional<Content> getContentById(Long id) {
        return contentRepository.findById(id);
    }

    @Override
    public Content updateContent(Content content) {
        return contentRepository.save(content);
    }

    @Override
    @Transactional
    public Content updateContentFromRequest(Long id, ContentRequest request) {
        Content content = contentRepository.findById(id).orElseThrow();
        mapRequestToContent(request, content);
        return contentRepository.save(content);
    }

    @Override
    public void deleteContent(Long id) {
        contentRepository.deleteById(id);
    }

    @Override
    public ContentResponse toResponse(Content content) {
        ContentResponse response = new ContentResponse();
        response.setId(content.getId());
        response.setTitle(content.getTitle());
        response.setDescription(content.getDescription());
        response.setCreatorId(content.getCreator() != null ? content.getCreator().getId() : null);
        response.setCreatorName(content.getCreator() != null ? content.getCreator().getFullName() : null);
        response.setStatus(content.getStatus() != null ? content.getStatus().name() : null);
        response.setDifficulty(content.getDifficulty() != null ? content.getDifficulty().name() : null);
        response.setRequiredTechnologies(content.getRequiredTechnologies());
        response.setMaxParticipants(content.getMaxParticipants());
        response.setStartDate(content.getStartDate());
        response.setEndDate(content.getEndDate());
        response.setCreatedAt(content.getCreatedAt());
        response.setUpdatedAt(content.getUpdatedAt());
        return response;
    }

    private void mapRequestToContent(ContentRequest request, Content content) {
        content.setTitle(request.getTitle());
        content.setDescription(request.getDescription());
        if (request.getCreatorId() != null) {
            User creator = userRepository.findById(request.getCreatorId()).orElseThrow();
            content.setCreator(creator);
        }
        if (request.getStatus() != null) {
            content.setStatus(ContentStatus.valueOf(request.getStatus()));
        }
        if (request.getDifficulty() != null) {
            content.setDifficulty(DifficultyLevel.valueOf(request.getDifficulty()));
        }
        content.setRequiredTechnologies(request.getRequiredTechnologies());
        content.setMaxParticipants(request.getMaxParticipants());
        content.setStartDate(request.getStartDate());
        content.setEndDate(request.getEndDate());
    }
}

