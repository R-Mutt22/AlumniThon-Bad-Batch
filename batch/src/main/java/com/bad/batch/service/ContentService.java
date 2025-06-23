package com.bad.batch.service;

import com.bad.batch.dto.request.ContentRequest;
import com.bad.batch.dto.response.ContentResponse;
import com.bad.batch.model.entities.Content;
import java.util.List;
import java.util.Optional;

public interface ContentService {
    Content createContent(Content content);
    Content createContentFromRequest(ContentRequest request);
    List<Content> getAllContents();
    Optional<Content> getContentById(Long id);
    Content updateContent(Content content);
    Content updateContentFromRequest(Long id, ContentRequest request);
    void deleteContent(Long id);
    ContentResponse toResponse(Content content);
}
