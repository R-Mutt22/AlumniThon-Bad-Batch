package com.bad.batch.exceptions.profileExceptionsHandling;

public class ProfileAlreadyExistsException extends RuntimeException {
    public ProfileAlreadyExistsException(Long userId) {
        super("El usuario con ID " + userId + " ya tiene un perfil existente");
    }
    
    public ProfileAlreadyExistsException(String message) {
        super(message);
    }
}
