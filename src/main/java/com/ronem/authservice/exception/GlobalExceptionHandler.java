/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:27/01/2026
 * Time:15:16
 */


package com.ronem.authservice.exception;

import com.ronem.authservice.model.response.ApiErrorResponse;
import com.ronem.authservice.model.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    // Http-Status: 401
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(org.springframework.security.core.AuthenticationException ae) {
        log.warn("Authentication failed: {}", ae.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(false, HttpStatus.UNAUTHORIZED, "Authentication failed: ", Instant.now());
        return new ResponseEntity<>(errorResponse, errorResponse.errorCode());
    }

    // Http-Status: 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ae) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(false, HttpStatus.FORBIDDEN, "Forbidden", Instant.now());
        return new ResponseEntity<>(errorResponse, errorResponse.errorCode());
    }

    // Http-Status: 404
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NoHandlerFoundException ne) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(false, HttpStatus.NOT_FOUND, "Resource you are looking for is not found", Instant.now());
        return new ResponseEntity<>(errorResponse, errorResponse.errorCode());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateKey(
            DataIntegrityViolationException ex
    ) {

        ApiErrorResponse errorResponse = new ApiErrorResponse(false, HttpStatus.CONFLICT, "Duplicate entry", Instant.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    //Jakarta validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage())
                );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, "failed", errors));
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExists(
            UserAlreadyExistException ex) {

        ApiErrorResponse errorResponse =
                new ApiErrorResponse(
                        false,
                        HttpStatus.CONFLICT,
                        ex.getMessage(),
                        Instant.now()
                );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


    // Http-Status: 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex) {
        ApiErrorResponse errorResponse =
                new ApiErrorResponse(false,
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred", Instant.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}