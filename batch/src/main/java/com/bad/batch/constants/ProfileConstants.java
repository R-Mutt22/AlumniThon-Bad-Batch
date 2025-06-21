package com.bad.batch.constants;

import java.util.Set;

public final class ProfileConstants {

    // Constructor privado para evitar instanciación
    private ProfileConstants() {}

    // Tecnologías válidas (inmutables)
    public static final Set<String> VALID_TECHNOLOGIES = Set.of(
            "JAVA", "PYTHON", "JAVASCRIPT", "TYPESCRIPT", "REACT", "VUE", "ANGULAR",
            "NODEJS", "SPRING_BOOT", "DJANGO", "FLASK", "DOCKER", "KUBERNETES",
            "AWS", "AZURE", "GCP", "MYSQL", "POSTGRESQL", "MONGODB", "REDIS"
    );

    // Intereses válidos (inmutables)
    public static final Set<String> VALID_INTERESTS = Set.of(
            "BACKEND", "FRONTEND", "FULLSTACK", "DEVOPS", "CLOUD", "AI_ML",
            "MOBILE", "GAME_DEVELOPMENT", "DATA_SCIENCE", "CYBERSECURITY",
            "UI_UX", "ARCHITECTURE", "TESTING"
    );

    // Límites de validación
    public static final int MAX_BIO_LENGTH = 500;
    public static final int MAX_LOCATION_LENGTH = 100;
    public static final int MAX_OBJECTIVES_LENGTH = 1000;
    public static final int MAX_TECHNOLOGIES = 10;
    public static final int MAX_INTERESTS = 5;
    public static final int DEFAULT_PAGE_SIZE = 20; // Valor más práctico para paginación

    // Métodos de acceso
    public static Set<String> getValidTechnologies() {
        return VALID_TECHNOLOGIES;
    }

    public static Set<String> getValidInterests() {
        return VALID_INTERESTS;
    }
}