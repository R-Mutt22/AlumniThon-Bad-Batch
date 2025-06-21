package com.bad.batch.service;

import com.bad.batch.dto.security.LoginRequest;
import com.bad.batch.dto.security.TokenResponse;
import com.bad.batch.dto.security.UserRegistrationRequest;
import com.bad.batch.dto.security.UserRegistrationResponse;

public interface AuthService {

    TokenResponse createUser (UserRegistrationRequest userRegistrationRequest);

    TokenResponse login (LoginRequest loginRequest);

    UserRegistrationResponse getUserDetails(Long userId);

}
