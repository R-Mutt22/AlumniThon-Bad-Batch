package com.bad.batch.service.impl;

import com.bad.batch.constants.ProfileConstants;
import com.bad.batch.dto.request.UpdateProfileRequest;
import com.bad.batch.exceptions.profileExceptionsHandling.InvalidInterestException;
import com.bad.batch.exceptions.profileExceptionsHandling.InvalidTechnologyException;
import com.bad.batch.exceptions.profileExceptionsHandling.ProfileVisibilityException;
import com.bad.batch.model.entities.Profile;
import com.bad.batch.model.enums.ProfileVisibility;
import com.bad.batch.repository.ProfileRepository;
import com.bad.batch.service.ProfileValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileValidationServiceImpl implements ProfileValidationService {

    private final ProfileRepository profileRepository;

    @Override
    public void validateProfileCreation(Long userId) {
        if (profileRepository.existsByUserId(userId)) {
            throw new IllegalStateException("El usuario ya tiene un perfil");
        }
    }

    @Override
    public void validateProfileUpdate(UpdateProfileRequest request) {
        Objects.requireNonNull(request, "La solicitud de actualización no puede ser nula");

        // Validación de bio
        if (request.bio() != null && request.bio().length() > 500) {
            throw new IllegalArgumentException("La biografía no puede exceder 500 caracteres");
        }

        // Validación de ubicación
        if (request.location() != null && request.location().length() > 100) {
            throw new IllegalArgumentException("La ubicación no puede exceder 100 caracteres");
        }

        // Validación de URLs
        validateUrl(request.githubUrl(), "URL de GitHub");
        validateUrl(request.linkedinUrl(), "URL de LinkedIn");
        validateUrl(request.personalWebsite(), "Sitio web personal");

        // Validación de tecnologías e intereses
        if (request.technologies() != null) {
            validateTechnologies(request.technologies());
        }
        if (request.interests() != null) {
            validateInterests(request.interests());
        }

        // Validación de objetivos
        if (request.objectives() != null && request.objectives().length() > 1000) {
            throw new IllegalArgumentException("Los objetivos no pueden exceder 1000 caracteres");
        }
    }

    @Override
    public void validateProfileVisibility(Long requesterId, Long profileUserId, Profile profile) {
        if (profile.getVisibility() == ProfileVisibility.PRIVATE &&
                !profileUserId.equals(requesterId)) {
            throw new ProfileVisibilityException(profile.getId());
        }
    }

    @Override
    public void validateUrl(String url, String fieldName) {
        if (url != null && !url.isBlank()) {
            try {
                new URL(url);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(fieldName + " no es válida");
            }
        }
    }

    // Validación de tecnologías optimizada
    public void validateTechnologies(Set<String> technologies) {
        if (technologies == null || technologies.isEmpty()) {
            return;
        }

        if (technologies.size() > ProfileConstants.MAX_TECHNOLOGIES) {
            throw new IllegalArgumentException(
                    String.format("Se permiten máximo %d tecnologías", ProfileConstants.MAX_TECHNOLOGIES)
            );
        }

        Set<String> invalidTechs = technologies.stream()
                .filter(tech -> !ProfileConstants.VALID_TECHNOLOGIES.contains(tech.toUpperCase()))
                .collect(Collectors.toSet());

        if (!invalidTechs.isEmpty()) {
            throw new InvalidTechnologyException(
                    String.format("Tecnologías no válidas: %s. Tecnologías válidas: %s",
                            invalidTechs,
                            String.join(", ", ProfileConstants.VALID_TECHNOLOGIES))
            );
        }
    }


    // Validación de intereses optimizada
    public void validateInterests(Set<String> interests) {
        if (interests == null || interests.isEmpty()) {
            return;
        }

        if (interests.size() > ProfileConstants.MAX_INTERESTS) {
            throw new IllegalArgumentException(
                    String.format("Se permiten máximo %d intereses", ProfileConstants.MAX_INTERESTS)
            );
        }

        Set<String> invalidInterests = interests.stream()
                .filter(interest -> !ProfileConstants.VALID_INTERESTS.contains(interest.toUpperCase()))
                .collect(Collectors.toSet());

        if (!invalidInterests.isEmpty()) {
            throw new InvalidInterestException(
                    String.format("Intereses no válidos: %s. Intereses válidos: %s",
                            invalidInterests,
                            String.join(", ", ProfileConstants.VALID_INTERESTS))
            );
        }
    }
}