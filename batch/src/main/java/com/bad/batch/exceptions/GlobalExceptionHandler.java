package com.bad.batch.exceptions;

import com.bad.batch.dto.response.ErrorResponse;
import com.bad.batch.exceptions.profileExceptionsHandling.InvalidInterestException;
import com.bad.batch.exceptions.profileExceptionsHandling.InvalidTechnologyException;
import com.bad.batch.exceptions.profileExceptionsHandling.ProfileNotFoundException;
import com.bad.batch.exceptions.profileExceptionsHandling.ProfileVisibilityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProfileNotFound(ProfileNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                "PROFILE_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InvalidTechnologyException.class, InvalidInterestException.class})
    public ResponseEntity<ErrorResponse> handleInvalidData(RuntimeException ex) {
        ErrorResponse response = new ErrorResponse(
                "INVALID_DATA",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProfileVisibilityException.class)
    public ResponseEntity<ErrorResponse> handleProfileVisibility(ProfileVisibilityException ex) {
        ErrorResponse response = new ErrorResponse(
                "PROFILE_ACCESS_DENIED",
                ex.getMessage(),
                HttpStatus.FORBIDDEN.value()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        // Si el mensaje contiene "email ya esta registrado", devolver 409 (Conflict)
        if (ex.getMessage() != null && ex.getMessage().contains("email ya esta registrado")) {
            ErrorResponse response = new ErrorResponse(
                    "USER_ALREADY_EXISTS",
                    ex.getMessage(),
                    HttpStatus.CONFLICT.value()
            );
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
        
        // Para otros IllegalArgumentException, devolver 400 (Bad Request)
        ErrorResponse response = new ErrorResponse(
                "INVALID_REQUEST",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
