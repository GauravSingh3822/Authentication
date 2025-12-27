package com.nexDew.Authentication.service.impl;

import com.nexDew.Authentication.dto.RequestDto.LoginRequestDto;
import com.nexDew.Authentication.dto.RequestDto.RefreshTokenRequest;
import com.nexDew.Authentication.dto.RequestDto.SignupRequestDto;
import com.nexDew.Authentication.dto.ResponseDto.LoginResponseDto;
import com.nexDew.Authentication.dto.ResponseDto.SignupResponseDto;
import com.nexDew.Authentication.entity.AuthEntity;
import com.nexDew.Authentication.entity.Permission;
import com.nexDew.Authentication.entity.RoleEntity;
import com.nexDew.Authentication.error.UserAlreadyExistException;
import com.nexDew.Authentication.repository.AuthRepository;
import com.nexDew.Authentication.repository.RoleEntityRepository;
import com.nexDew.Authentication.service.AuthService;
import com.nexDew.Authentication.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final AuthRepository authRepository;
    private final RoleEntityRepository roleRepository;
    private final JwtTokenProvider tokenProvider;


    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthEntity user = (AuthEntity) authentication.getPrincipal();
        String accessToken = tokenProvider.generateToken(authentication);
        String refreshToken = authUtil.generateRefreshToken(user);

        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .type("Bearer")
                .uuid(user.getUuid())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoleEntities().stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toSet()))
                .permissions(authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(auth-> !auth.startsWith("ROLE_"))
                        .collect(Collectors.toSet()))
                .build();
        return loginResponseDto;
    }

    @Override
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {

        // 1️Check if user already exists
        authRepository.findByUsername(signupRequestDto.getUsername())
                .ifPresent(u -> {
                    throw new UserAlreadyExistException("User already exists with username: "
                            + signupRequestDto.getUsername());
                });

        // 2️Fetch default ROLE_USER from DB
        RoleEntity userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() ->
                        new RuntimeException("Default role ROLE_USER not found"));

        // 3️Build AuthEntity
        AuthEntity userEntity = AuthEntity.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .email(signupRequestDto.getEmail())
                .phoneNumber(signupRequestDto.getPhoneNumber())
                .build();

        // 4️ssign role
        userEntity.getRoleEntities().add(userRole);
        userRole.getAuthEntities().add(userEntity);

        // 5️Save user
        AuthEntity savedUser = authRepository.save(userEntity);

        // 6️Log
        log.info("User registered successfully | id={} | username={} | roles={}",
                savedUser.getUuid(),
                savedUser.getUsername(),
                savedUser.getRoleEntities()
                        .stream()
                        .map(RoleEntity::getName)
                        .toList()
        );

        //  Response
        return new SignupResponseDto(
                savedUser.getUuid(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber()
        );
    }
    @Override
    public String refreshToken(RefreshTokenRequest refreshToken) {
        if(!authUtil.validateToken(refreshToken.getRefreshToken())){
            throw new RuntimeException("Invalid Refresh Token");
        }
        String usernameFromToken = authUtil.getUsernameFromToken(refreshToken.getRefreshToken());
        AuthEntity authEntity = authRepository.findByUsername(usernameFromToken).orElseThrow();
        return authUtil.generateAccessToken(authEntity);
    }

}
