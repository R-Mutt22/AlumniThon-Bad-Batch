package com.bad.batch.controller;

import com.bad.batch.dto.ProfileSearchCriteria;
import com.bad.batch.dto.request.CreateProfileRequest;
import com.bad.batch.dto.request.UpdateProfileRequest;
import com.bad.batch.dto.response.ProfileResponse;
import com.bad.batch.dto.response.ProfileSearchResponse;
import com.bad.batch.model.enums.ExperienceLevel;
import com.bad.batch.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// No necesitamos ResponseStatusException importado aquí si no se usa directamente en el controlador.

import java.util.Set;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@Tag(name = "Profiles", description = "Gestión de perfiles de usuario") // Agregamos una etiqueta general para el controlador
public class ProfileController {

    private final ProfileService profileService;

    @Operation(
            summary = "Crear un nuevo perfil de usuario",
            description = "Permite a un usuario autenticado crear su perfil. El ID de usuario se extrae del token JWT.",
            security = @SecurityRequirement(name = "JWT"), // Indica que este endpoint requiere JWT
            responses = {
                    @ApiResponse(responseCode = "201", description = "Perfil creado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos o intereses/tecnologías no válidos",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "No autenticado (token JWT ausente o inválido)",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "409", description = "Conflicto: El usuario ya tiene un perfil", // Asumiendo esta posible validación
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<ProfileResponse> createProfile(
            @Parameter(description = "Datos del perfil a crear", required = true)
            @Valid @RequestBody CreateProfileRequest request,
            @Parameter(hidden = true) // Oculta este parámetro en la documentación Swagger, ya que se inyecta internamente
            @RequestAttribute("X-User-Id") Long userId) {

        ProfileResponse response = profileService.createProfile(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Operation(
            summary = "Obtener el perfil del usuario autenticado",
            description = "Recupera el perfil completo del usuario que está autenticado.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil recuperado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResponse.class))),
                    @ApiResponse(responseCode = "401", description = "No autenticado",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Perfil no encontrado para el usuario autenticado",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(
            @Parameter(hidden = true)
            @RequestAttribute("X-User-Id") Long userId) {
        return ResponseEntity.ok(profileService.getMyProfile(userId));
    }


    @Operation(
            summary = "Obtener el perfil público de otro usuario",
            description = "Permite recuperar el perfil público de un usuario específico por su ID. La visibilidad del perfil determinará si se puede acceder.",
            security = @SecurityRequirement(name = "JWT"),
            parameters = {
                    @Parameter(name = "userId", description = "ID del usuario cuyo perfil se desea obtener", required = true, in = ParameterIn.PATH)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil recuperado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResponse.class))),
                    @ApiResponse(responseCode = "401", description = "No autenticado",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (perfil no público o usuario no autorizado)",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Usuario o perfil no encontrado",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getUserProfile(
            @PathVariable Long userId,
            @Parameter(hidden = true)
            @RequestAttribute("X-User-Id") Long requesterId) {
        return ResponseEntity.ok(profileService.getUserProfile(userId));
    }



    @Operation(
            summary = "Actualizar el perfil del usuario autenticado",
            description = "Permite a un usuario autenticado actualizar los detalles de su propio perfil.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProfileResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "No autenticado",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Perfil no encontrado para el usuario autenticado",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> updateMyProfile(
            @Parameter(description = "Datos del perfil a actualizar", required = true)
            @Valid @RequestBody UpdateProfileRequest request,
            @Parameter(hidden = true)
            @RequestAttribute("X-User-Id") Long userId) {
        return ResponseEntity.ok(profileService.updateMyProfile(userId, request));
    }



    @Operation(
            summary = "Buscar perfiles de usuario con filtros",
            description = "Permite buscar y filtrar perfiles de usuario por varios criterios como consulta general, tecnologías, nivel de experiencia, ubicación e intereses. Soporta paginación y ordenamiento.",
            security = @SecurityRequirement(name = "JWT"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Búsqueda exitosa, devuelve una página de perfiles",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class, subTypes = {ProfileSearchResponse.class}))),
                    @ApiResponse(responseCode = "400", description = "Parámetros de búsqueda inválidos",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "No autenticado",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/search")
    public ResponseEntity<Page<ProfileSearchResponse>> searchProfiles(
            @Parameter(description = "Término de búsqueda general (bio, nombres, etc.)")
            @RequestParam(required = false) String query,
            @Parameter(description = "Lista de tecnologías (ej. JAVA, SPRING_BOOT)")
            @RequestParam(required = false) Set<String> technologies,
            @Parameter(description = "Nivel de experiencia (ej. JUNIOR, INTERMEDIATE, SENIOR)", schema = @Schema(implementation = ExperienceLevel.class))
            @RequestParam(required = false) String experienceLevel,
            @Parameter(description = "Ubicación del usuario (ej. 'Ciudad de México, México')")
            @RequestParam(required = false) String location,
            @Parameter(description = "Lista de intereses (ej. BACKEND, DEVOPS)")
            @RequestParam(required = false) Set<String> interests,
            @Parameter(description = "Número de página (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Campo por el cual ordenar (ej. 'createdAt', 'lastName')", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Dirección de ordenamiento (ASC o DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        ProfileSearchCriteria criteria = ProfileSearchCriteria.builder()
                .query(query)
                .technologies(technologies)
                .experienceLevel(experienceLevel != null ?
                        ExperienceLevel.valueOf(experienceLevel) : null)
                .location(location)
                .interests(interests)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        return ResponseEntity.ok(profileService.searchProfiles(criteria));
    }


    @Operation(
            summary = "Obtener lista de tecnologías válidas",
            description = "Devuelve todas las tecnologías predefinidas que pueden ser asociadas a un perfil.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de tecnologías obtenida exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Set.class, subTypes = {String.class}))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/technologies")
    public ResponseEntity<Set<String>> getValidTechnologies() {
        return ResponseEntity.ok(profileService.getValidTechnologies());
    }


    @Operation(
            summary = "Obtener lista de intereses válidos",
            description = "Devuelve todos los intereses predefinidos que pueden ser asociados a un perfil.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de intereses obtenida exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Set.class, subTypes = {String.class}))),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/interests")
    public ResponseEntity<Set<String>> getValidInterests() {
        return ResponseEntity.ok(profileService.getValidInterests());
    }
}