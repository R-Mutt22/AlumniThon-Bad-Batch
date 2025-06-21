package com.bad.batch.exceptions.profileExceptionsHandling;

import java.util.Set;

public class InvalidInterestException extends RuntimeException{

    public InvalidInterestException(Set<String> invalidInterests) {
        super("Intereses no válidos: " + invalidInterests);
    }

    public InvalidInterestException(String message) {
        super(message);
    }

    public InvalidInterestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInterestException(Throwable cause) {
        super(cause);
    }
}
