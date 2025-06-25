package com.bad.batch.exceptions;

import com.bad.batch.dto.response.ErrorResponse;
import com.bad.batch.exceptions.profileExceptionsHandling.InvalidInterestException;
import com.bad.batch.exceptions.profileExceptionsHandling.InvalidTechnologyException;
import com.bad.batch.exceptions.profileExceptionsHandling.ProfileNotFoundException;
import com.bad.batch.exceptions.profileExceptionsHandling.ProfileVisibilityException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
                "USER_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFound(Exception ex) {
        ErrorResponse response = new ErrorResponse(
                "RESOURCE_NOT_FOUND",
                ex.getMessage() != null ? ex.getMessage() : "El recurso solicitado no existe",
                HttpStatus.NOT_FOUND.value()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Error de validación"
                ));

        String errorMessage = "Errores de validación en los campos: " +
                fieldErrors.entrySet().stream()
                        .map(entry -> entry.getKey() + " (" + entry.getValue() + ")")
                        .collect(Collectors.joining(", "));

        ErrorResponse response = new ErrorResponse(
                "VALIDATION_ERROR",
                errorMessage,
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        ErrorResponse response = new ErrorResponse(
                "INVALID_REQUEST_BODY",
                "El cuerpo de la solicitud es inválido o no se puede leer correctamente",
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParameter(MissingServletRequestParameterException ex) {
        ErrorResponse response = new ErrorResponse(
                "MISSING_PARAMETER",
                "Falta el parámetro requerido: " + ex.getParameterName(),
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorResponse response = new ErrorResponse(
                "INVALID_PARAMETER_TYPE",
                "El parámetro '" + ex.getName() + "' tiene un formato incorrecto",
                HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ErrorResponse response = new ErrorResponse(
                "DATA_INTEGRITY_VIOLATION",
                "Operación no permitida debido a restricciones de integridad de datos",
                HttpStatus.CONFLICT.value()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex) {
        ErrorResponse response = new ErrorResponse(
                "DATABASE_ERROR",
                "Error al acceder a la base de datos",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
        // Registrar el error con stack trace para depuración
        System.err.println("Error de referencia nula: " + ex.getMessage());
        ex.printStackTrace();

        ErrorResponse response = new ErrorResponse(
                "SERVER_ERROR",
                "Se produjo un error en el servidor debido a una referencia nula",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Manejador de excepciones genérico que captura cualquier otra excepción no manejada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        // Registrar el error para depuración
        System.err.println("Error no manejado: " + ex.getClass().getName() + ": " + ex.getMessage());
        ex.printStackTrace();

        ErrorResponse response = new ErrorResponse(
                "SERVER_ERROR",
                "Se produjo un error inesperado en el servidor",
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
