package com.bad.batch.dto.security;

import com.bad.batch.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserRegistrationResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private UserRole role; // El tipo de dato para el rol (usamos el enum UserRole)
        private Boolean isActive;
}
