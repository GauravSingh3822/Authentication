package com.nexDew.Authentication.dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponeDto {
    private String accesstoken;
    private String refreshToken;
    private UUID uuid;
    private String username;
    private String email;
    private String phoneNumber;



}
