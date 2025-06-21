package com.bad.batch.dto.mappers;

import com.bad.batch.dto.request.CreateProfileRequest;
import com.bad.batch.dto.request.UpdateProfileRequest;
import com.bad.batch.dto.response.ProfileResponse;
import com.bad.batch.dto.response.ProfileSearchResponse;
import com.bad.batch.model.entities.Profile;
import com.bad.batch.model.enums.ProfileVisibility;

import java.util.Set;

public class ProfileMapper {

     //Método para convertir CreateProfileRequest → Profile (Nuevo)
    public static Profile toProfileFromCreateRequest(CreateProfileRequest request) {
        return Profile.builder()
                .bio(request.getBio())
                .location(request.getLocation())
                .githubUrl(request.getGithubUrl())
                .linkedinUrl(request.getLinkedinUrl())
                .personalWebsite(request.getPersonalWebsite())
                .experienceLevel(request.getExperienceLevel())
                .visibility(request.getVisibility() != null ?
                        request.getVisibility() : ProfileVisibility.PUBLIC) // Default
                .technologies(request.getTechnologies() != null ?
                        request.getTechnologies() : Set.of()) // Evita NullPointer
                .interests(request.getInterests() != null ?
                        request.getInterests() : Set.of())
                .objectives(request.getObjectives())
                .build();
    }

    public static ProfileResponse toProfileResponse(Profile profile) {
        return ProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .bio(profile.getBio())
                .location(profile.getLocation())
                .githubUrl(profile.getGithubUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .personalWebsite(profile.getPersonalWebsite())
                .experienceLevel(profile.getExperienceLevel())
                .visibility(profile.getVisibility())
                .technologies(profile.getTechnologies())
                .interests(profile.getInterests())
                .objectives(profile.getObjectives())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    public static ProfileSearchResponse toSearchResponse(Profile profile) {
        return ProfileSearchResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .firstName(profile.getUser().getFirstName())
                .lastName(profile.getUser().getLastName())
                .location(profile.getLocation())
                .experienceLevel(profile.getExperienceLevel())
                .technologies(profile.getTechnologies())
                .interests(profile.getInterests())
                .build();
    }

    public static void updateProfileFromRequest(UpdateProfileRequest request, Profile profile) {
        profile.setBio(request.bio());
        profile.setLocation(request.location());
        profile.setGithubUrl(request.githubUrl());
        profile.setLinkedinUrl(request.linkedinUrl());
        profile.setPersonalWebsite(request.personalWebsite());
        profile.setExperienceLevel(request.experienceLevel());
        profile.setVisibility(request.visibility());
        profile.setTechnologies(request.technologies());
        profile.setInterests(request.interests());
        profile.setObjectives(request.objectives());
    }
}