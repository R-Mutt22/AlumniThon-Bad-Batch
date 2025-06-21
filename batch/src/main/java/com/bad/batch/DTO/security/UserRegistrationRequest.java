package com.bad.batch.DTO.security;

import com.bad.batch.Enum.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    @NotBlank(message = "El nombre no puede estar vacío.")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío.")
    private String lastName;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "Debe ser un formato de email válido.")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres.")
    @Pattern(
        regexp = "^(?=.*\\p{Ll})(?=.*\\p{Lu})(?=.*\\d)(?=.*[@$!%*?&])[\\p{L}\\d@$!%*?&]{8,}$",
        message = "La contraseña no cumple con los requisitos de seguridad: debe contener al menos una minúscula, una mayúscula, un número y un carácter especial."
    )
    private String password;

    @NotNull(message = "El campo 'role' no puede estar vacío")
    private UserRole role;
}
