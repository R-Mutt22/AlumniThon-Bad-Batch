package com.bad.batch.service;

import com.bad.batch.dto.request.UpdateProfileRequest;
import com.bad.batch.exceptions.profileExceptionsHandling.InvalidInterestException;
import com.bad.batch.exceptions.profileExceptionsHandling.InvalidTechnologyException;
import com.bad.batch.exceptions.profileExceptionsHandling.ProfileVisibilityException;
import com.bad.batch.model.entities.Profile;

import java.util.Set;

public interface ProfileValidationService {

    /**
     * Valida que un usuario pueda crear un perfil
     * @param userId ID del usuario a validar
     * @throws IllegalStateException si el usuario ya tiene un perfil
     */
    void validateProfileCreation(Long userId);

    /**
     * Valida los datos de actualización de un perfil
     * @param request DTO con los datos a actualizar
     * @throws IllegalArgumentException si algún campo no cumple las validaciones
     * @throws InvalidTechnologyException si contiene tecnologías no válidas
     * @throws InvalidInterestException si contiene intereses no válidos
     */
    void validateProfileUpdate(UpdateProfileRequest request);

    /**
     * Valida la visibilidad de un perfil
     * @param requesterId ID del usuario que hace la solicitud
     * @param profileUserId ID del usuario dueño del perfil
     * @param profile Entidad del perfil a validar
     * @throws ProfileVisibilityException si el solicitante no tiene permisos para ver el perfil
     */
    void validateProfileVisibility(Long requesterId, Long profileUserId, Profile profile);

    /**
     * Valida una URL
     * @param url URL a validar
     * @param fieldName Nombre del campo para el mensaje de error
     * @throws IllegalArgumentException si la URL no es válida
     */
    void validateUrl(String url, String fieldName);

    /**
     * Valida un conjunto de tecnologías
     * @param technologies Conjunto de tecnologías a validar
     * @throws IllegalArgumentException si excede el máximo permitido
     * @throws InvalidTechnologyException si contiene tecnologías no válidas
     */
    void validateTechnologies(Set<String> technologies);

    /**
     * Valida un conjunto de intereses
     * @param interests Conjunto de intereses a validar
     * @throws IllegalArgumentException si excede el máximo permitido
     * @throws InvalidInterestException si contiene intereses no válidos
     */
    void validateInterests(Set<String> interests);
}
