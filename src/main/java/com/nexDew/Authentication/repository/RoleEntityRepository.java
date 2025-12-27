package com.nexDew.Authentication.repository;

import com.nexDew.Authentication.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.Set;

public interface RoleEntityRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String roleUser);

    int FindAllById(Set<Long> roleIds);
}