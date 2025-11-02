package com.nexDew.Authentication.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ApiError {
    private final String message;       // Human-readable message
    private final String errorCode;     // Stable error code for client mapping
    private final HttpStatus status;    // HTTP status
    private final LocalDateTime timestamp;
    private final String path;          // Requested endpoint (for debugging)
    private final String traceId;       // Mainly used for Zipkin and Eureka server
}
