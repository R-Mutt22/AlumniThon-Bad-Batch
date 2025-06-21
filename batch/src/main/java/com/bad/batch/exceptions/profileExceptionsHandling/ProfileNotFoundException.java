package com.bad.batch.exceptions.profileExceptionsHandling;

public class ProfileNotFoundException extends RuntimeException{

    public ProfileNotFoundException(Long id) {
        super("Perfil no encontrado con ID: " + id);
    }

    public ProfileNotFoundException(String message) {
        super(message);
    }

    public ProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProfileNotFoundException(Throwable cause) {
        super(cause);
    }
}
