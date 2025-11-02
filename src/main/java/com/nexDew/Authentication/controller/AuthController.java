package com.nexDew.Authentication.controller;

import com.nexDew.Authentication.dto.RequestDto.LoginRequestDto;
import com.nexDew.Authentication.dto.RequestDto.RefreshTokenRequest;
import com.nexDew.Authentication.dto.RequestDto.SignupRequestDto;
import com.nexDew.Authentication.dto.ResponseDto.LoginResponeDto;
import com.nexDew.Authentication.dto.ResponseDto.SignupResponseDto;
import com.nexDew.Authentication.entity.AuthEntity;
import com.nexDew.Authentication.repository.AuthRepository;
import com.nexDew.Authentication.service.AuthService;
import com.nexDew.Authentication.util.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication related to Users")
public class AuthController {
    private final AuthService authService;
    private final AuthUtil authUtil;
    private final AuthRepository authRepository;

    @Operation(summary = "Login User")
    @PostMapping("/login")
    public ResponseEntity<LoginResponeDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authService.login(loginRequestDto));

    }

    @Operation(summary = "Signup User")
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto){
        return ResponseEntity.ok(authService.signup(signupRequestDto));

    }
    @Operation(summary = "Refresh Token")
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody RefreshTokenRequest refreshToken) {
        String newAccessToken = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
