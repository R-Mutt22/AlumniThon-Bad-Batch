package com.bad.batch.Service;

import com.bad.batch.DTO.security.TokenResponse;
import io.jsonwebtoken.Claims;

public interface JwtService {
    TokenResponse generateToken (Long userId, String role);
    Claims getClaims(String token);
    boolean isExpired(String token);
    Long extractUserId(String token);
    String extractRole(String token);

}
