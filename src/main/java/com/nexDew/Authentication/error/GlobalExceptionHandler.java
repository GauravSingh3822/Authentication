package com.nexDew.Authentication.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //Username not found (Spring Security)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFound(UsernameNotFoundException ex,
                                                           HttpServletRequest request) {
        return buildErrorResponse("User not Found", "AUTH-404", HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiError> handleUserAlreadyExist(UserAlreadyExistException ex,
                                                           HttpServletRequest request) {
        return buildErrorResponse("User Already Present", "USR-409", HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(PasswordAlreadyExistException.class)
    public ResponseEntity<ApiError> handlePasswordAlreadyExist(PasswordAlreadyExistException ex,
                                                           HttpServletRequest request) {
        return buildErrorResponse("Password Already Present", "PAS-409", HttpStatus.CONFLICT, request);
    }

    // Invalid credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex,
                                                         HttpServletRequest request) {
        return buildErrorResponse("Invalid username or password", "AUTH-401", HttpStatus.UNAUTHORIZED, request);
    }

    // Forbidden access
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex,
                                                       HttpServletRequest request) {
        return buildErrorResponse("Access Denied", "AUTH-403", HttpStatus.FORBIDDEN, request);
    }

    // Validation errors (e.g. @Valid DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex,
                                                     HttpServletRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return buildErrorResponse(validationErrors.toString(), "VAL-400", HttpStatus.BAD_REQUEST, request);
    }

    // Generic fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex,
                                                           HttpServletRequest request) {
        return buildErrorResponse("Something went wrong. Please try again later.",
                "GEN-500", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // Utility method to build consistent error response
    private ResponseEntity<ApiError> buildErrorResponse(String message,
                                                        String errorCode,
                                                        HttpStatus status,
                                                        HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString(); // Replace with Sleuth/Zipkin if available

        ApiError apiError = ApiError.builder()
                .message(message)
                .errorCode(errorCode)
                .status(status)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        log.error("TraceId: {}, ErrorCode: {}, Status: {}, Path: {}", traceId, errorCode, status, request.getRequestURI());

        return ResponseEntity.status(status).body(apiError);
    }
}

