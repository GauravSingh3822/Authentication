package com.nexDew.Authentication.repository;

import com.nexDew.Authentication.entity.AuthEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<AuthEntity, UUID> {
    Optional<AuthEntity> findByUsername(String username);
    Optional<AuthEntity> findByEmail(String email);
    Optional<AuthEntity> findByPhoneNumber(String phoneNumber);

    @Query("SELECT U FROM AuthEntity U JOIN FETCH U.roleEntities R JOIN FETCH R.permissions WHERE U.username = :username")
    Optional<AuthEntity> findByUsernameWithRolesAndPermission(String username);

    @Query("select u from AuthEntity u join fetch u.roleEntities where u.uuid = :id")
    Optional<AuthEntity> findByIdWithRoles(UUID id);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
