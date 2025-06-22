package com.bad.batch.controller.security;

import com.bad.batch.dto.security.LoginRequest;
import com.bad.batch.dto.security.TokenResponse;
import com.bad.batch.dto.security.UserRegistrationRequest;
import com.bad.batch.dto.security.UserRegistrationResponse;
import com.bad.batch.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    // ¡ESTE ES EL METODO QUE CAMBIA PARA RESOLVER EL ERROR!
    public ResponseEntity<UserRegistrationResponse> getUser(@RequestAttribute(name = "X-User-Id") String userId) {
        Long id;
        try {
            // Es crucial convertir el ID de String a Long, ya que el servicio lo espera como Long
            id = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            // Esto debería ser raro si tu filtro JWT siempre pone un ID numérico,
            // pero es buena práctica manejar una posible conversión fallida.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID de usuario inválido en el token.");
        }

        // ¡AHORA LLAMAMOS AL SERVICIO PARA OBTENER LOS DETALLES COMPLETOS DEL USUARIO!
        UserRegistrationResponse userResponse = authService.getUserDetails(id);
        return ResponseEntity.ok(userResponse);
    }
}
