package com.nexDew.Authentication.service.impl;

import com.nexDew.Authentication.dto.RequestDto.LoginRequestDto;
import com.nexDew.Authentication.dto.RequestDto.RefreshTokenRequest;
import com.nexDew.Authentication.dto.RequestDto.SignupRequestDto;
import com.nexDew.Authentication.dto.ResponseDto.LoginResponeDto;
import com.nexDew.Authentication.dto.ResponseDto.SignupResponseDto;
import com.nexDew.Authentication.entity.AuthEntity;
import com.nexDew.Authentication.error.UserAlreadyExistException;
import com.nexDew.Authentication.repository.AuthRepository;
import com.nexDew.Authentication.service.AuthService;
import com.nexDew.Authentication.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final AuthRepository authRepository;

    @Override
    public LoginResponeDto login(LoginRequestDto loginRequestDto) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()));

        AuthEntity authEntity = (AuthEntity) authenticate.getPrincipal();
        String accessToken = authUtil.generateAccesToken(authEntity);
        String refreshToken = authUtil.generateRefreshToken(authEntity);

        return new LoginResponeDto(
                accessToken,
                refreshToken,
                authEntity.getUuid(),
                authEntity.getUsername(),
                authEntity.getEmail(),
                authEntity.getPhoneNumber());

    }

    @Override
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
     // check if user already exists
        authRepository.findByUsername(signupRequestDto.getUsername())
                .ifPresent(u -> { throw new UserAlreadyExistException("User Already Present"); });

        // build entity
        AuthEntity userEntity = AuthEntity.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .email(signupRequestDto.getEmail())
                .phoneNumber(signupRequestDto.getPhoneNumber())
                .roles("ROLE_USER")
                .build();

        // save to DB
        AuthEntity savedUser = authRepository.save(userEntity);

        // log for debugging
        log.info("Saved User: id={}, username={}, role={}",
                savedUser.getUuid(), savedUser.getUsername(), savedUser.getRoles());

        return new SignupResponseDto(savedUser.getUuid(),savedUser.getUsername(),savedUser.getEmail(),savedUser.getPhoneNumber());
    }

    @Override
    public String refreshToken(RefreshTokenRequest refreshToken) {
        if(!authUtil.validateToken(refreshToken.getRefreshToken())){
            throw new RuntimeException("Invalid Refresh Token");
        }
        String usernameFromToken = authUtil.getUsernameFromToken(refreshToken.getRefreshToken());
        AuthEntity authEntity = authRepository.findByUsername(usernameFromToken).orElseThrow();
        return authUtil.generateAccesToken(authEntity);
    }

//    public String refreshToken(String refreshToken) {
//        if(!authUtil.validateToken(refreshToken)){
//            throw new RuntimeException("Invalid Refresh Token");
//        }
//        String usernameFromToken = authUtil.getUsernameFromToken(refreshToken);
//        AuthEntity authEntity = authRepository.findByUsername(usernameFromToken).orElseThrow();
//        return authUtil.generateAccesToken(authEntity);
//    }
}
