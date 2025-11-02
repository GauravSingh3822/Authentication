package com.nexDew.Authentication.repository;

import com.nexDew.Authentication.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<AuthEntity, UUID> {
    Optional<AuthEntity> findByUsername(String username);
}
