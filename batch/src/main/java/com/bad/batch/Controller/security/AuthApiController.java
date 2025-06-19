package com.bad.batch.Controller.security;

import com.bad.batch.DTO.security.LoginRequest;
import com.bad.batch.DTO.security.TokenResponse;
import com.bad.batch.DTO.security.UserRegistrationRequest;
import com.bad.batch.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthApiController implements AuthApi{

    private final AuthService authService;

    @Override
    public ResponseEntity<TokenResponse> createUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
        TokenResponse response = authService.createUser(userRegistrationRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // Devolvemos 201 CREATED para un registro exitoso
    }

    @Override
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<String> getUser(@RequestAttribute(name = "X-User-Id") String userId) {
        // Simplemente devuelve el userId como String, como en tu ejemplo.
        // Aquí no necesitas llamar a authService.getUserDetails(id) para obtener el objeto User completo,
        // ya que la especificación de este endpoint en la interfaz es solo devolver el ID como String.
        return ResponseEntity.ok(userId);
    }
}
