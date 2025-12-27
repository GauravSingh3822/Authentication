package com.nexDew.Authentication.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * @param message   Human-readable message
 * @param errorCode Stable error code for client mapping
 * @param status    HTTP status
 * @param path      Requested endpoint (for debugging)
 * @param traceId   Mainly used for Zipkin and Eureka server
 */

@Builder
public record ApiError(String message, ErrorCode errorCode, HttpStatus status, LocalDateTime timestamp, String path,
                       String traceId) {
}


