package com.nexDew.Authentication.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {

    private String accessToken;
    private String refreshToken;

    private UUID uuid;
    private String username;
    private String email;
    private String phoneNumber;
    private String type = "Bearer";

    private Set<String> roles;
    private Set<String> permissions;
}