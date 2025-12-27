package com.nexDew.Authentication.service;

import com.nexDew.Authentication.dto.RequestDto.LoginRequestDto;
import com.nexDew.Authentication.dto.RequestDto.RefreshTokenRequest;
import com.nexDew.Authentication.dto.RequestDto.SignupRequestDto;
import com.nexDew.Authentication.dto.ResponseDto.LoginResponseDto;
import com.nexDew.Authentication.dto.ResponseDto.SignupResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    SignupResponseDto signup(SignupRequestDto signupRequestDto);

    String refreshToken(RefreshTokenRequest refreshToken);
}
