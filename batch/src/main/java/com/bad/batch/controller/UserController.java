package com.bad.batch.controller;

import java.util.List;

import com.bad.batch.dto.request.UserDTO;
import com.bad.batch.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@Tag(name = "Users", description = "Gestión y consulta de usuarios") // Tag for the controller
public class UserController {

    private final UserService getUserService;

    @Autowired
    public UserController(UserService getUserService) {
        this.getUserService = getUserService;
    }

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Recupera una lista de todos los usuarios registrados en el sistema. " +
                    "**Nota:** Este endpoint puede requerir autenticación y/o roles de administrador " +
                    "dependiendo de tu configuración de seguridad (no se especifica aquí si lleva `@SecurityRequirement`).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class, type = "array"))), // Schema for a list
                    @ApiResponse(responseCode = "401", description = "No autenticado (si requiere JWT)",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (si requiere roles específicos)",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = getUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}