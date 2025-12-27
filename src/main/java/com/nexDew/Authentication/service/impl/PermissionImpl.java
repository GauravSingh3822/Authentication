package com.nexDew.Authentication.service.impl;

import com.nexDew.Authentication.annotation.RequirePermission;
import com.nexDew.Authentication.dto.PermissionDTO;
import com.nexDew.Authentication.entity.type.ResourceType;
import com.nexDew.Authentication.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionImpl implements PermissionService {
    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        return null;
    }

    @Override
    public List<PermissionDTO> getAllPermission() {
        return List.of();
    }

    @Override
    public Set<PermissionDTO> getAllActivePermission() {
        return Set.of();
    }

    @Override
    public PermissionDTO getPermissionById(Long uuid) {
        return null;
    }

    @Override
    public PermissionDTO getPermissionByName(String name) {
        return null;
    }

    @Override
    public List<PermissionDTO> getPermissionByResourceType(String resourceType) {
        return List.of();
    }

    @Override
    public PermissionDTO updatePermission(Long id, PermissionDTO permissionDTO) {
        return null;
    }

    @Override
    public void deletePermission(Long id) {

    }
}
