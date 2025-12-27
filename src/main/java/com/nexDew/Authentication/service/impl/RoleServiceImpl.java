package com.nexDew.Authentication.service.impl;

import com.nexDew.Authentication.dto.CreateRoleRequest;
import com.nexDew.Authentication.dto.PermissionDTO;
import com.nexDew.Authentication.dto.RoleDTO;
import com.nexDew.Authentication.dto.UpdateRoleRequest;
import com.nexDew.Authentication.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {
    @Override
    public RoleDTO getRoleById(Long id) {
        return null;
    }

    @Override
    public List<RoleDTO> getRolesByName(String name) {
        return List.of();
    }

    @Override
    public RoleDTO createRole(CreateRoleRequest request) {
        return null;
    }

    @Override
    public RoleDTO updateRole(Long id, UpdateRoleRequest request) {
        return null;
    }

    @Override
    public void deleteRole(Long id) {

    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return List.of();
    }

    @Override
    public RoleDTO assignPermissionToRole(Long roleId, Set<Long> permissionIds) {
        return null;
    }

    @Override
    public RoleDTO removePermissionFromRole(Long roleId, Set<Long> permissionIds) {
        return null;
    }

    @Override
    public Set<PermissionDTO> getRolePermissions(Long roleId) {
        return Set.of();
    }
}
