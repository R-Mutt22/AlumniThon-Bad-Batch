package com.bad.batch.service;

import com.bad.batch.dto.ProfileSearchCriteria;
import com.bad.batch.dto.request.CreateProfileRequest;
import com.bad.batch.dto.request.UpdateProfileRequest;
import com.bad.batch.dto.response.ProfileResponse;
import com.bad.batch.dto.response.ProfileSearchResponse;
import org.springframework.data.domain.Page;

import java.util.Set;

public interface ProfileService {

    ProfileResponse createProfile(Long userId, CreateProfileRequest request);

    ProfileResponse getMyProfile(Long userId);

    ProfileResponse getUserProfile(Long userId);

    ProfileResponse updateMyProfile(Long userId, UpdateProfileRequest request);

    Page<ProfileSearchResponse> searchProfiles(ProfileSearchCriteria criteria);

    Set<String> getValidTechnologies();

    Set<String> getValidInterests();



}
