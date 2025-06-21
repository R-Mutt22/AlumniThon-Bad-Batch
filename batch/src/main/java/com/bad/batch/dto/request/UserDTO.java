package com.bad.batch.dto.request;

import com.bad.batch.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "Debe ser un formato de email válido.")
    private String email;

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío.")
    private String lastName;

    @NotNull(message = ("El rol no puede ser nulo."))
    private UserRole role;

    @NotNull(message = ("El estado 'isActive' no puede ser nulo."))
    private Boolean isActive;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
