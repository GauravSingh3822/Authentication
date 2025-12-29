package com.nexDew.Authentication.error;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class APIResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime time;

    public static <T> APIResponse<T> success(String message, T data) {
        return APIResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .time(LocalDateTime.now())
                .build();
    }

    public static <T> APIResponse<T> error(T data) {
        return APIResponse.<T>builder()
                .success(false)
                .message("Request failed")
                .data(data)
                .time(LocalDateTime.now())
                .build();
    }
}
