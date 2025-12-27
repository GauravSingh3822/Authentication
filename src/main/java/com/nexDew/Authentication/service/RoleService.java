package com.nexDew.Authentication.service;

import com.nexDew.Authentication.dto.CreateRoleRequest;
import com.nexDew.Authentication.dto.PermissionDTO;
import com.nexDew.Authentication.dto.RoleDTO;
import com.nexDew.Authentication.dto.UpdateRoleRequest;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

public interface RoleService {
    RoleDTO getRoleById(Long id);

    List<RoleDTO> getRolesByName(String name);

    RoleDTO createRole(@Valid CreateRoleRequest request);

    RoleDTO updateRole(Long id, @Valid UpdateRoleRequest request);

    void deleteRole(Long id);

    List<RoleDTO> getAllRoles();

    RoleDTO assignPermissionToRole(Long roleId, Set<Long> permissionIds);

    RoleDTO removePermissionFromRole(Long roleId, Set<Long> permissionIds);

    Set<PermissionDTO> getRolePermissions(Long roleId);
}
