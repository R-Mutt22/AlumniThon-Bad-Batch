package com.bad.batch.exceptions.profileExceptionsHandling;

import java.util.Set;

public class InvalidTechnologyException extends RuntimeException{
    public InvalidTechnologyException(Set<String> invalidTechnologies) {
        super("Tecnologías no válidas: " + invalidTechnologies);
    }

    public InvalidTechnologyException(String message) {
        super(message);
    }

    public InvalidTechnologyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTechnologyException(Throwable cause) {
        super(cause);
    }
}
