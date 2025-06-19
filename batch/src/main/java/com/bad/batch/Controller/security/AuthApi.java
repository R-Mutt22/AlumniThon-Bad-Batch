package com.bad.batch.Controller.security;

import com.bad.batch.DTO.security.LoginRequest;
import com.bad.batch.DTO.security.TokenResponse;
import com.bad.batch.DTO.security.UserRegistrationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints para autenticación de usuarios")
@RequestMapping("/api/auth")
public interface AuthApi {


    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Crea una cuenta de usuario y devuelve un token JWT",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"), // Cambiado a 201 Created
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "409", description = "El usuario con este email ya existe") // Añadido para conflicto
            }
    )
    @PostMapping("/register")
    ResponseEntity<TokenResponse> createUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest);
    // NOTA: Tu ejemplo usaba UserEntityRequest, pero seguiremos usando UserRegistrationRequest que ya tenemos.

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario y devuelve un token JWT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
            }
    )
    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest);

    @Operation(
            summary = "Obtener ID de usuario autenticado",
            description = "Devuelve el ID del usuario extraído del token JWT que está en la cabecera.",
            security = @SecurityRequirement(name = "JWT"), // Indica que este endpoint requiere JWT
            responses = {
                    @ApiResponse(responseCode = "200", description = "ID obtenido exitosamente"),
                    @ApiResponse(responseCode = "401", description = "Token inválido o no proporcionado")
            }
    )
    @GetMapping // Mapea a /api/auth (GET)
    @PreAuthorize("isAuthenticated()") // Requiere que el usuario esté autenticado
    ResponseEntity<String> getUser(@RequestAttribute(name = "X-User-Id") String userId);
    // NOTA: Eliminado @Valid de String userId, ya que no aplica aquí.
}
