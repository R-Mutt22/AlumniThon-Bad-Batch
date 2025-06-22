package com.bad.batch.dto.request;

import com.bad.batch.model.enums.ExperienceLevel;
import com.bad.batch.model.enums.ProfileVisibility;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CreateProfileRequest {
    @Size(max = 500, message = "La biografía no puede exceder 500 caracteres")
    private String bio;

    private String location;

    @Size(max = 100, message = "La URL de GitHub no puede exceder 100 caracteres")
    private String githubUrl;

    @Size(max = 100, message = "La URL de LinkedIn no puede exceder 100 caracteres")
    private String linkedinUrl;

    @Size(max = 100, message = "El sitio web personal no puede exceder 100 caracteres")
    private String personalWebsite;

    private ExperienceLevel experienceLevel;

    private ProfileVisibility visibility;

    @Size(max = 10, message = "Máximo 10 tecnologías permitidas")
    private Set<String> technologies;

    @Size(max = 5, message = "Máximo 5 intereses permitidos")
    private Set<String> interests;

    @Size(max = 1000, message = "Los objetivos no pueden exceder 1000 caracteres")
    private String objectives;
}