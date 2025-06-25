package com.bad.batch.service.impl;

import com.bad.batch.dto.request.ChallengeSubmissionRequest;
import com.bad.batch.dto.request.ContentRequest;
import com.bad.batch.dto.response.ChallengeSubmissionResponse;
import com.bad.batch.dto.response.ContentResponse;
import com.bad.batch.model.entities.*;
import com.bad.batch.model.enums.*;
import com.bad.batch.repository.*;
import com.bad.batch.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContentServiceImpl implements ContentService {
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;
    private final ChallengeSubmissionRepository challengeSubmissionRepository;

    @Autowired
    public ContentServiceImpl(ContentRepository contentRepository, UserRepository userRepository, ParticipationRepository participationRepository, ChallengeSubmissionRepository challengeSubmissionRepository) {
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
        this.participationRepository = participationRepository;
        this.challengeSubmissionRepository = challengeSubmissionRepository;
    }

    @Override
    public Content createContent(Content content) {
        return contentRepository.save(content);
    }

    @Override
    @Transactional
    public Content createContentFromRequest(ContentRequest request) {
        // Validaciones de reglas de negocio
        if (request.getEndDate() != null && request.getStartDate() != null && !request.getEndDate().isAfter(request.getStartDate())) {
            throw new IllegalArgumentException("La fecha de finalización debe ser posterior a la fecha de inicio.");
        }
        if (request.getStartDate() != null && request.getStartDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new IllegalArgumentException("La fecha de inicio debe ser al menos 1 hora en el futuro.");
        }
        if (request.getType() == ContentType.MENTORSHIP) {
            if (request.getMentorshipType() == null) {
                throw new IllegalArgumentException("El tipo de mentoría es obligatorio.");
            }
            if (request.getDurationMinutes() == null) {
                throw new IllegalArgumentException("La duración es obligatoria para mentorías.");
            }
            if (request.getMentorshipType() == MentorshipType.ONE_ON_ONE && request.getMaxParticipants() != null && request.getMaxParticipants() != 1) {
                throw new IllegalArgumentException("Para mentoría ONE_ON_ONE, maxParticipants debe ser 1.");
            }
        }
        if (request.getType() == ContentType.CHALLENGE) {
            if (request.getProblemStatement() == null || request.getProblemStatement().isBlank()) {
                throw new IllegalArgumentException("El enunciado del problema es obligatorio para challenges.");
            }
            if (request.getAcceptanceCriteria() == null || request.getAcceptanceCriteria().isBlank()) {
                throw new IllegalArgumentException("Los criterios de aceptación son obligatorios para challenges.");
            }
            if (request.getChallengeType() == null) {
                throw new IllegalArgumentException("El tipo de challenge es obligatorio.");
            }
        }
        Content content;
        if (request.getType() == ContentType.MENTORSHIP) {
            Mentorship mentorship = new Mentorship();
            mapRequestToContent(request, mentorship);
            mentorship.setDurationMinutes(request.getDurationMinutes());
            mentorship.setMentorshipType(request.getMentorshipType());
            mentorship.setIsLive(request.getIsLive() != null ? request.getIsLive() : false);
            // Campos requeridos por herencia de tabla única
            mentorship.setAllowsTeams(false); // Siempre false para mentorías
            mentorship.setAcceptanceCriteria("N/A - Mentorship");
            mentorship.setProblemStatement("N/A - Mentorship");
            content = mentorship;
        } else if (request.getType() == ContentType.CHALLENGE) {
            Challenge challenge = new Challenge();
            mapRequestToContent(request, challenge);
            challenge.setProblemStatement(request.getProblemStatement());
            challenge.setAcceptanceCriteria(request.getAcceptanceCriteria());
            challenge.setAllowsTeams(request.getAllowsTeams() != null ? request.getAllowsTeams() : false);
            challenge.setChallengeType(request.getChallengeType());
            // Campos requeridos por herencia de tabla única
            challenge.setDurationMinutes(0); // 0 indica que no aplica para challenges
            challenge.setIsLive(false);
            challenge.setMentorshipType(MentorshipType.ONE_ON_ONE); // Valor por defecto para challenges
            content = challenge;
        } else {
            content = new Content();
            mapRequestToContent(request, content);
        }
        content.setType(request.getType());
        content.setStatus(ContentStatus.DRAFT); // Forzar status DRAFT al crear
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
        if (content instanceof Mentorship && request.getType() == ContentType.MENTORSHIP) {
            mapRequestToContent(request, content);
            ((Mentorship) content).setDurationMinutes(request.getDurationMinutes());
            ((Mentorship) content).setMentorshipType(request.getMentorshipType());
            ((Mentorship) content).setIsLive(request.getIsLive() != null ? request.getIsLive() : false);
            // Campos requeridos por herencia de tabla única
            ((Mentorship) content).setAllowsTeams(false); // Siempre false para mentorías
            ((Mentorship) content).setAcceptanceCriteria("N/A - Mentorship");
            ((Mentorship) content).setProblemStatement("N/A - Mentorship");
        } else if (content instanceof Challenge && request.getType() == ContentType.CHALLENGE) {
            mapRequestToContent(request, content);
            ((Challenge) content).setProblemStatement(request.getProblemStatement());
            ((Challenge) content).setAcceptanceCriteria(request.getAcceptanceCriteria());
            ((Challenge) content).setAllowsTeams(request.getAllowsTeams() != null ? request.getAllowsTeams() : false);
            ((Challenge) content).setChallengeType(request.getChallengeType());
            // Campos requeridos por herencia de tabla única
            ((Challenge) content).setDurationMinutes(0); // 0 indica que no aplica para challenges
            ((Challenge) content).setIsLive(false);
            ((Challenge) content).setMentorshipType(null);
        } else {
            mapRequestToContent(request, content);
        }
        content.setType(request.getType());
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
        response.setType(content.getType());
        // Mapear campos específicos según el tipo
        if (content instanceof Mentorship mentorship) {
            response.setDurationMinutes(mentorship.getDurationMinutes());
            response.setMentorshipType(mentorship.getMentorshipType());
            response.setIsLive(mentorship.getIsLive());
        } else if (content instanceof Challenge challenge) {
            response.setProblemStatement(challenge.getProblemStatement());
            response.setAcceptanceCriteria(challenge.getAcceptanceCriteria());
            response.setAllowsTeams(challenge.getAllowsTeams());
            response.setChallengeType(challenge.getChallengeType());
        }
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

    @Override
    public List<Content> getAllContentsForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return contentRepository.findAll().stream()
                .filter(c -> c.getStatus() == ContentStatus.PUBLISHED)
                .toList();
        }
        return contentRepository.findAll().stream()
            .filter(c -> c.getStatus() == ContentStatus.PUBLISHED || (c.getCreator() != null && c.getCreator().getId().equals(userId)))
            .toList();
    }

    @Override
    public List<Content> searchContents(ContentType type, String tech, String difficulty, int page, int size, Long userId) {
        List<Content> all = getAllContentsForUser(userId);
        return all.stream()
            .filter(c -> type == null || c.getType() == type)
            .filter(c -> tech == null || (c.getRequiredTechnologies() != null && c.getRequiredTechnologies().stream().anyMatch(t -> t.equalsIgnoreCase(tech))))
            .filter(c -> difficulty == null || (c.getDifficulty() != null && c.getDifficulty().name().equalsIgnoreCase(difficulty)))
            .skip((long) page * size)
            .limit(size)
            .toList();
    }

    @Override
    @Transactional
    public Content publishContent(Long id, Long userId) {
        Content content = contentRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Contenido no encontrado"));
        if (!content.getCreator().getId().equals(userId)) {
            throw new SecurityException("Solo el creador puede publicar este contenido.");
        }
        if (content.getStatus() == ContentStatus.PUBLISHED) {
            throw new IllegalStateException("El contenido ya está publicado y no puede volver a DRAFT.");
        }
        if (content.getStartDate() == null || content.getStartDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new IllegalArgumentException("La fecha de inicio debe ser futura (al menos 1 hora desde ahora) para publicar.");
        }
        // Validar campos obligatorios
        if (content.getTitle() == null || content.getTitle().isBlank() || content.getTitle().length() > 100) {
            throw new IllegalArgumentException("El título es obligatorio y debe tener máximo 100 caracteres.");
        }
        if (content.getDescription() == null || content.getDescription().isBlank() || content.getDescription().length() > 2000) {
            throw new IllegalArgumentException("La descripción es obligatoria y debe tener máximo 2000 caracteres.");
        }
        if (content.getRequiredTechnologies() == null || content.getRequiredTechnologies().isEmpty() || content.getRequiredTechnologies().size() > 5) {
            throw new IllegalArgumentException("Debe haber entre 1 y 5 tecnologías requeridas.");
        }
        if (content.getMaxParticipants() == null || content.getMaxParticipants() < 1 || content.getMaxParticipants() > 100) {
            throw new IllegalArgumentException("El campo maxParticipants es obligatorio (mínimo 1, máximo 100).");
        }
        if (content.getType() == null) {
            throw new IllegalArgumentException("El tipo de contenido es obligatorio.");
        }
        if (content.getType() == ContentType.MENTORSHIP) {
            Mentorship mentorship = (Mentorship) content;
            if (mentorship.getDurationMinutes() == null || mentorship.getDurationMinutes() < 30 || mentorship.getDurationMinutes() > 480) {
                throw new IllegalArgumentException("La duración de la mentoría debe ser entre 30 y 480 minutos.");
            }
            if (mentorship.getMentorshipType() == null) {
                throw new IllegalArgumentException("El tipo de mentoría es obligatorio.");
            }
            if (mentorship.getMentorshipType() == MentorshipType.ONE_ON_ONE && content.getMaxParticipants() != 1) {
                throw new IllegalArgumentException("Si el tipo es ONE_ON_ONE, maxParticipants debe ser 1.");
            }
        }
        if (content.getType() == ContentType.CHALLENGE) {
            Challenge challenge = (Challenge) content;
            if (challenge.getProblemStatement() == null || challenge.getProblemStatement().isBlank() || challenge.getProblemStatement().length() > 5000) {
                throw new IllegalArgumentException("El enunciado del challenge es obligatorio y debe tener máximo 5000 caracteres.");
            }
            if (challenge.getAcceptanceCriteria() == null || challenge.getAcceptanceCriteria().isBlank() || challenge.getAcceptanceCriteria().length() > 2000) {
                throw new IllegalArgumentException("Los criterios de aceptación son obligatorios y deben tener máximo 2000 caracteres.");
            }
            if (challenge.getChallengeType() == null) {
                throw new IllegalArgumentException("El tipo de challenge es obligatorio.");
            }
        }
        content.setStatus(ContentStatus.PUBLISHED);
        return contentRepository.save(content);
    }

    @Override
    public void joinContent(Long contentId, Long userId) {

    }

    @Override
    public void leaveContent(Long contentId, Long userId) {

    }

    @Override
    public List<Long> getParticipants(Long contentId) {
        return List.of();
    }

    @Override
    public ChallengeSubmissionResponse submitChallenge(Long contentId, Long userId, ChallengeSubmissionRequest request) {
        return null;
    }

    @Override
    public List<ChallengeSubmissionResponse> getChallengeSubmissions(Long contentId, Long userId) {
        return List.of();
    }

    @Override
    public void startContent(Long contentId) {

    }

    @Override
    public void removeParticipant(Long contentId, Long userId, Long creatorId) {

    }
}
