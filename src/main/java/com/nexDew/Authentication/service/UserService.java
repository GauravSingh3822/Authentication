package com.nexDew.Authentication.service;

import com.nexDew.Authentication.dto.AuthEntityDTO;
import com.nexDew.Authentication.dto.CreateUserRequest;
import com.nexDew.Authentication.dto.PermissionDTO;
import com.nexDew.Authentication.dto.UpdateUserRequest;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    List<AuthEntityDTO> getAllUsers();

    AuthEntityDTO getUserById(UUID uuid);

    AuthEntityDTO createUser(CreateUserRequest request);

    AuthEntityDTO updateUser(UUID id, UpdateUserRequest request);

    void deleteUser(UUID id);

    AuthEntityDTO assignRolesToUser(UUID id, Set<Long> roleIds);

    AuthEntityDTO removeRolesFromUser(UUID id, Set<Long> roleIds);

    Set<PermissionDTO> getUserPermissions(UUID id);

    boolean userHasPermission(UUID id, String permissionName);
}
