package com.bad.batch.service;

import com.bad.batch.dto.request.ContentRequest;
import com.bad.batch.dto.response.ContentResponse;
import com.bad.batch.model.entities.Content;
import com.bad.batch.dto.request.ChallengeSubmissionRequest;
import com.bad.batch.dto.response.ChallengeSubmissionResponse;
import com.bad.batch.model.enums.ContentType;
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
    Content publishContent(Long id, Long userId);
    void joinContent(Long contentId, Long userId);
    void leaveContent(Long contentId, Long userId);
    List<Long> getParticipants(Long contentId);
    ChallengeSubmissionResponse submitChallenge(Long contentId, Long userId, ChallengeSubmissionRequest request);
    List<ChallengeSubmissionResponse> getChallengeSubmissions(Long contentId, Long userId);
    void startContent(Long contentId);
    void removeParticipant(Long contentId, Long userId, Long creatorId);
    List<Content> searchContents(ContentType type, String tech, String difficulty, int page, int size, Long userId);
    List<Content> getAllContentsForUser(Long userId);
}
