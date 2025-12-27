package com.nexDew.Authentication.mapper;

import com.nexDew.Authentication.dto.AuthEntityDTO;
import com.nexDew.Authentication.dto.PermissionDTO;
import com.nexDew.Authentication.dto.RoleDTO;
import com.nexDew.Authentication.entity.AuthEntity;
import com.nexDew.Authentication.entity.Permission;
import com.nexDew.Authentication.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ModelMapper {
    public AuthEntityDTO convertDTO(AuthEntity authEntity){
        Set<RoleDTO> roleDTOs = authEntity.getRoleEntities().stream()
                .map(this::convertRoleToDTO)
                .collect(Collectors.toSet());
        Set<PermissionDTO> permissionDTOs = authEntity.getAllPermission().stream()
                .map(this::convertPermissionToDTO)
                .collect(Collectors.toSet());

        return AuthEntityDTO.builder()
                .id(authEntity.getUuid())
                .username(authEntity.getUsername())
                .email(authEntity.getEmail())
                .firstName(authEntity.getFirstName())
                .lastName(authEntity.getLastName())
                .roles(roleDTOs)
                .permission(permissionDTOs)
                .enabled(authEntity.getEnabled())
                .lastLogin(authEntity.getLastLogin())
                .createdAt(authEntity.getCreatedAt())
                .updatedAt(authEntity.getUpdatedAt())
                .build();


    }
    public RoleDTO convertRoleToDTO(RoleEntity roleEntity){
        return RoleDTO.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName())
                .description(roleEntity.getDescription())
                .active(roleEntity.getActive())
                .build();
    }

    public PermissionDTO convertPermissionToDTO(Permission permission){
        return PermissionDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .actionType(permission.getActionType())
                .resourceType(permission.getResourceType())
                .active(permission.getActive())
                .build();
    }
}
