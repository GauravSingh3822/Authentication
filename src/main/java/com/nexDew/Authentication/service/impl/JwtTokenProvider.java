package com.nexDew.Authentication.service.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    // Generate JWT Token from Authentication
    public String generateToken(Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("permission", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return generateTokenFromUsername(userDetails.getUsername(),claims);

    }
    // Generate JWT token from username
    public String generateTokenFromUsername(String username){
        return generateTokenFromUsername(username,new HashMap<>());
    }

    // Generate Token from username with custom claims
    public String generateTokenFromUsername(String username,Map<String,Object> claims){
        Date now = new Date();
        Date expriyDate = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .subject(username)
                .claims(claims)
                .issuedAt(now)
                .expiration(expriyDate)
                .signWith(getSigningKey())
                .compact();
    }
    // Get Username from Jwt Token
    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }
    // Get Signing key
    public SecretKey getSigningKey(){
        byte[] bytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);

    }

    // Validate JWT Token
    private boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        }catch (SignatureException exception){
            log.error("Invalid Jwt signature: {}",exception.getMessage());
        }catch (MalformedJwtException exception){
            log.error("Invalid Jwt token: {}",exception.getMessage());
        }catch (ExpiredJwtException exception){
            log.error("Jwt token is expired signature: {}",exception.getMessage());
        }catch (UnsupportedJwtException exception){
            log.error("Jwt token is unsupported: {}",exception.getMessage());
        }catch (IllegalArgumentException exception){
            log.error("Jwt claims string is empty : {}",exception.getMessage());
        }
        return false;
    }
    private Date getExpriationDateFromToken(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
}
