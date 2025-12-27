package com.nexDew.Authentication.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
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

    /* ================= USER EXISTS ================= */

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<APIResponse<ApiError>> handleUserAlreadyExist(
            UserAlreadyExistException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                ex.getMessage(),
                ErrorCode.USER_ALREADY_EXISTS,
                HttpStatus.CONFLICT,
                request
        );
    }

    /* ================= PASSWORD EXISTS ================= */

    @ExceptionHandler(PasswordAlreadyExistException.class)
    public ResponseEntity<APIResponse<ApiError>> handlePasswordAlreadyExist(
            PasswordAlreadyExistException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                ex.getMessage(),
                ErrorCode.PASSWORD_ALREADY_EXISTS,
                HttpStatus.CONFLICT,
                request
        );
    }

    /* ================= BAD CREDENTIALS ================= */

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResponse<ApiError>> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                "Invalid username or password",
                ErrorCode.AUTH_INVALID_CREDENTIALS,
                HttpStatus.UNAUTHORIZED,
                request
        );
    }

    /* ================= ACCESS DENIED ================= */

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse<ApiError>> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {

        return buildErrorResponse(
                "Access denied",
                ErrorCode.ACCESS_DENIED,
                HttpStatus.FORBIDDEN,
                request
        );
    }

    /* ================= VALIDATION ERRORS ================= */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<ApiError>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return buildErrorResponse(
                validationErrors.toString(),
                ErrorCode.VALIDATION_FAILED,
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    /* ================= GENERIC FALLBACK ================= */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<ApiError>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unhandled exception", ex);

        return buildErrorResponse(
                "Something went wrong. Please try again later.",
                ErrorCode.INTERNAL_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

    /* ================= COMMON BUILDER ================= */

    private ResponseEntity<APIResponse<ApiError>> buildErrorResponse(
            String message,
            ErrorCode errorCode,
            HttpStatus status,
            HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();

        ApiError apiError = ApiError.builder()
                .message(message)
                .errorCode(errorCode)
                .status(status)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .traceId(traceId)
                .build();

        log.error(
                "TraceId={} | ErrorCode={} | Status={} | Path={}",
                traceId, errorCode, status, request.getRequestURI()
        );

        return ResponseEntity
                .status(status)
                .body(APIResponse.error("Request failed"));
    }
}