package com.bad.batch.exceptions.profileExceptionsHandling;

public class ProfileVisibilityException extends RuntimeException{
    public ProfileVisibilityException(Long profileId) {
        super("No tienes permiso para ver el perfil con ID: " + profileId);
    }

    public ProfileVisibilityException(String message) {
        super(message);
    }

    public ProfileVisibilityException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileVisibilityException(Throwable cause) {
        super(cause);
    }
}
