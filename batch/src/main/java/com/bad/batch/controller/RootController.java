package com.bad.batch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@Tag(name = "Root", description = "Endpoint raíz de la aplicación")
public class RootController {

    @GetMapping
    @Operation(summary = "Endpoint raíz", 
               description = "Endpoint principal que muestra información básica del servidor SkillLink")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servidor funcionando correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Map<String, Object>> root() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("application", "SkillLink API");
            response.put("version", "1.0.0");
            response.put("status", "OK");
            response.put("timestamp", LocalDateTime.now());
            response.put("message", "Welcome to SkillLink API - Mentorship and Challenges Platform");
            response.put("documentation", "/swagger-ui.html");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error en endpoint raíz: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "SERVER_ERROR");
            errorResponse.put("message", "Se produjo un error inesperado en el servidor");
            errorResponse.put("timestamp", LocalDateTime.now());
            
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
