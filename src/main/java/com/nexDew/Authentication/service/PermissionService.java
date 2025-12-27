package com.nexDew.Authentication.service;

import com.nexDew.Authentication.dto.PermissionDTO;
import com.nexDew.Authentication.entity.Permission;
import com.nexDew.Authentication.entity.type.ResourceType;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PermissionService {
    PermissionDTO createPermission(PermissionDTO permissionDTO);
    List<PermissionDTO> getAllPermission();
    Set<PermissionDTO> getAllActivePermission();
    PermissionDTO getPermissionById(Long uuid);
    PermissionDTO getPermissionByName(String name);
    List<PermissionDTO> getPermissionByResourceType(String resourceType);
    PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO);
    void deletePermission(Long id);
}
