package com.bad.batch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bad.batch.service.KeepAliveService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@RestController
@RequestMapping("/api/keep-alive")
@Tag(name = "Keep Alive", description = "Endpoint para mantener activo el servidor")
public class KeepAliveController {

    @Autowired
    private KeepAliveService keepAliveService;

    @GetMapping("/ping")
    @Operation(summary = "Ping para mantener activo el servidor", 
               description = "Endpoint simple que responde con el estado del servidor para evitar que se suspenda por inactividad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servidor activo"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> ping() {
        String status = keepAliveService.getServerStatus();
        return ResponseEntity.ok(status);
    }

    @GetMapping("/status")
    @Operation(summary = "Estado detallado del servidor", 
               description = "Obtiene información detallada sobre el estado del servidor y las estadísticas de keep-alive")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado obtenido exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> statusInfo = keepAliveService.getDetailedStatus();
        return ResponseEntity.ok(statusInfo);
    }
}
