package com.nexDew.Authentication.service.impl;

import com.nexDew.Authentication.entity.AuthEntity;
import com.nexDew.Authentication.repository.AuthRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthRepository authRepository;
    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthEntity authEntity = authRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username
                )
        );
        if(!authEntity.getEnabled()){
            throw  new UsernameNotFoundException("User account is disabled: "+username);
        }
        return authEntity;
    }
    @Transactional(readOnly=true)
    public UserDetails loadUserById(UUID id){
        return authRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("User not found with id: "+id));
    }
}
