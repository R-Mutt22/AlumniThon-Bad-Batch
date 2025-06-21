package com.bad.batch.dto.request;

import com.bad.batch.model.enums.ExperienceLevel;
import com.bad.batch.model.enums.ProfileVisibility;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record UpdateProfileRequest(
        @Size(max = 500, message = "La biografía no puede exceder 500 caracteres")
        String bio,

        @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
        String location,

        String githubUrl,

        String linkedinUrl,

        String personalWebsite,

        @NotNull(message = "El nivel de experiencia es requerido")
        ExperienceLevel experienceLevel,

        @NotNull(message = "La visibilidad del perfil es requerida")
        ProfileVisibility visibility,

        @Size(max = 10, message = "Máximo 10 tecnologías permitidas")
        Set<String> technologies,

        @Size(max = 5, message = "Máximo 5 intereses permitidos")
        Set<String> interests,

        @Size(max = 1000, message = "Los objetivos no pueden exceder 1000 caracteres")
        String objectives
) {}