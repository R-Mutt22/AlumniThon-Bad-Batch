package com.bad.batch.Service.impl;

import com.bad.batch.DTO.security.TokenResponse;
import com.bad.batch.Service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    private final SecretKey secretKey;
    private static final long EXPIRATION_TIME = 864_000_000;

    public JwtServiceImpl(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Arrays.copyOf(
                secret.getBytes(StandardCharsets.UTF_8),
                32 // Longitud exacta requerida para HS256
        );
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public TokenResponse generateToken(Long userId, String role) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        String normalizedRole = role.startsWith("ROLE_") ? role.substring(5) : role;

        String token = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("userId", userId)
                .claim("role", normalizedRole)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();

        return TokenResponse.builder()
                .accessToken(token)
                .build();
    }

    @Override
    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            System.err.println("Error Parsing JWT: " + e.getMessage());
            throw new IllegalArgumentException("Invalid JWT Token", e);
        }
    }

    @Override
    public boolean isExpired(String token) {
        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public Long extractUserId(String token) {
        try {
            Claims claims = getClaims(token);
            Object userIdClaim = claims.get("userId");
            if (userIdClaim == null) {
                throw new IllegalArgumentException("No userId claim found for token");
            }
            return ((Number) userIdClaim).longValue();
        } catch (Exception e) {
            System.err.println("Error extrayendo token: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }
}
