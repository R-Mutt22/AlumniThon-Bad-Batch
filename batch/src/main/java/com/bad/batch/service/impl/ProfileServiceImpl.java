package com.bad.batch.service.impl;

import com.bad.batch.constants.ProfileConstants;
import com.bad.batch.dto.ProfileSearchCriteria;
import com.bad.batch.dto.mappers.ProfileMapper;
import com.bad.batch.dto.request.CreateProfileRequest;
import com.bad.batch.dto.request.UpdateProfileRequest;
import com.bad.batch.dto.response.ProfileResponse;
import com.bad.batch.dto.response.ProfileSearchResponse;
import com.bad.batch.exceptions.UserNotFoundException;
import com.bad.batch.exceptions.profileExceptionsHandling.ProfileAlreadyExistsException;
import com.bad.batch.exceptions.profileExceptionsHandling.ProfileNotFoundException;
import com.bad.batch.model.entities.Profile;
import com.bad.batch.model.entities.User;
import com.bad.batch.repository.ProfileRepository;
import com.bad.batch.repository.UserRepository;
import com.bad.batch.service.ProfileService;
import com.bad.batch.service.ProfileValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileValidationService validationService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProfileResponse createProfile(Long userId, CreateProfileRequest request) {
        // 1. Validar que el usuario no tenga perfil existente
        if (profileRepository.existsByUserId(userId)) {
            throw new ProfileAlreadyExistsException(userId);
        }

        // 2. Obtener usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // 3. Validar datos del request (opcional, si usas validaciones adicionales)
        validationService.validateProfileCreation(userId);
        validationService.validateTechnologies(request.getTechnologies());
        validationService.validateInterests(request.getInterests());

        // 4. Convertir request → Entidad Profile
        Profile profile = ProfileMapper.toProfileFromCreateRequest(request);
        profile.setUser(user); // Asignar relación al usuario

        // 5. Guardar y retornar response
        Profile savedProfile = profileRepository.save(profile);
        return ProfileMapper.toProfileResponse(savedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile(Long userId) {
        Profile profile = profileRepository.findByUserIdWithRelations(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));
        return ProfileMapper.toProfileResponse(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getUserProfile(Long userId) {
        Profile profile = profileRepository.findByUserIdWithRelations(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));
        validationService.validateProfileVisibility(userId, profile.getUser().getId(), profile);
        return ProfileMapper.toProfileResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponse updateMyProfile(Long userId, UpdateProfileRequest request) {
        Profile profile = profileRepository.findByUserIdWithRelations(userId)
                .orElseThrow(() -> new ProfileNotFoundException(userId));

        validationService.validateProfileUpdate(request);
        ProfileMapper.updateProfileFromRequest(request, profile);

        Profile updatedProfile = profileRepository.save(profile);
        return ProfileMapper.toProfileResponse(updatedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProfileSearchResponse> searchProfiles(ProfileSearchCriteria criteria) {
        // Crear Pageable desde los parámetros del criteria
        Pageable pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                Sort.by(
                        criteria.getSortDirection().equalsIgnoreCase("DESC")
                                ? Sort.Direction.DESC
                                : Sort.Direction.ASC,
                        criteria.getSortBy()
                )
        );

        return profileRepository.findPublicProfilesWithFilters(
                criteria.getQuery(),
                criteria.getExperienceLevel(),
                criteria.getLocation(),
                criteria.getTechnologies(),
                criteria.getTechnologies() != null && !criteria.getTechnologies().isEmpty(),
                criteria.getInterests(),
                criteria.getInterests() != null && !criteria.getInterests().isEmpty(),
                pageable
        ).map(ProfileMapper::toSearchResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getValidTechnologies() {
        return ProfileConstants.VALID_TECHNOLOGIES;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getValidInterests() {
        return ProfileConstants.VALID_INTERESTS;
    }
}