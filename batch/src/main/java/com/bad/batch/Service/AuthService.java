package com.bad.batch.Service;

import com.bad.batch.DTO.security.LoginRequest;
import com.bad.batch.DTO.security.TokenResponse;
import com.bad.batch.DTO.security.UserRegistrationRequest;

public interface AuthService {

    TokenResponse createUser (UserRegistrationRequest userRegistrationRequest);

    TokenResponse login (LoginRequest loginRequest);

}
