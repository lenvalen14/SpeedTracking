package edu.ut.its.response;

import edu.ut.its.exceptions.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle MethodArgumentNotValidException for validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        // Get validation errors
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Wrap the response in a custom ResponseWrapper
        ResponseWrapper<Map<String, String>> response = new ResponseWrapper<>("Validation failed", errors);

        // Return BAD_REQUEST (400) status
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle custom application exceptions (like JWT exceptions)
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ResponseWrapper<String>> handleAppException(AppException ex) {
        ResponseWrapper<String> response = new ResponseWrapper<>(ex.getErrorCode().getMessage(), null);
        return new ResponseEntity<>(response, ex.getErrorCode().getStatusCode());
    }

    // Generic fallback for any uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseWrapper<String>> handleAllExceptions(Exception ex, WebRequest request) {
        ResponseWrapper<String> response = new ResponseWrapper<>("An unexpected error occurred: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
