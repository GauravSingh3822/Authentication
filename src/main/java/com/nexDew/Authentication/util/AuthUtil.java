package com.nexDew.Authentication.util;

import com.nexDew.Authentication.entity.AuthEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Component
public class AuthUtil {
    @Value("${jwt.secretKey}")
    private String  jwtSecretKey;

    private final long accessExpiration = 1000 * 60 * 10;   // 10 min
    private final long refreshExpiration = 1000 * 60 * 60;  // 1 hr


    @PostConstruct
    public SecretKey getsecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));

    }

    public String generateAccesToken(AuthEntity authEntity) {
        return Jwts.builder()
                .subject(authEntity.getUsername())
                .claim("UserId", authEntity.getUuid())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+accessExpiration)) //10 min
                .signWith(getsecretKey())
                .compact();
    }

    public String generateRefreshToken(AuthEntity authEntity) {
        return Jwts.builder()
                .subject(authEntity.getUsername())
                .claim("UserId", authEntity.getUuid())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+refreshExpiration)) //10 min
                .signWith(getsecretKey())
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getsecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch(BadCredentialsException ex){
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims payload = Jwts.parser()
                .verifyWith(getsecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return payload.getSubject();

    }

}
