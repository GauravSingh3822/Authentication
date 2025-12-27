package com.nexDew.Authentication.service.impl;

import com.nexDew.Authentication.dto.AuthEntityDTO;
import com.nexDew.Authentication.dto.CreateUserRequest;
import com.nexDew.Authentication.dto.PermissionDTO;
import com.nexDew.Authentication.dto.UpdateUserRequest;
import com.nexDew.Authentication.entity.AuthEntity;
import com.nexDew.Authentication.entity.Permission;
import com.nexDew.Authentication.entity.RoleEntity;
import com.nexDew.Authentication.mapper.ModelMapper;
import com.nexDew.Authentication.repository.AuthRepository;
import com.nexDew.Authentication.repository.PermissionRepository;
import com.nexDew.Authentication.repository.RoleEntityRepository;
import com.nexDew.Authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final AuthRepository authRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<AuthEntityDTO> getAllUsers() {
        log.debug("Fetching all users");
        List<AuthEntity> entities = authRepository.findAll();
        return entities.stream()
                .map(modelMapper::convertDTO)
                .collect(Collectors.toList());

    }

    @Override
    public AuthEntityDTO getUserById(UUID uuid) {
        log.debug("Fetching user by id={}", uuid);
        AuthEntity authEntity = authRepository.findById(uuid).orElseThrow(() -> new RuntimeException("User not found"));
        return modelMapper.convertDTO(authEntity);
    }

    @Override
    public AuthEntityDTO createUser(CreateUserRequest request) {
        log.debug("Creating user username={}", request.getUsername());

        if(authRepository.existsByUsername(request.getUsername())){
            log.warn("Duplicate username attempt: {}", request.getUsername());
            throw new RuntimeException("Username already exists: "+request.getUsername());

        }
        if(authRepository.existsByEmail(request.getEmail())) {
            log.warn("Duplicate email attempt: {}", request.getEmail());
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        AuthEntity authEntity = AuthEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .enabled(true)
                .accountNonExpried(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();

        // Assign Roles
        if(request.getRolesIds() !=null && !request.getRolesIds().isEmpty()) {
            Set<RoleEntity> roles = new HashSet<>(roleEntityRepository.findAllById(request.getRolesIds()));
            if (roles.size() != request.getRolesIds().size()) {
                throw new RuntimeException("Some roles were not found");
            }
            authEntity.setRoleEntities(roles);
        }
            AuthEntity saved = authRepository.save(authEntity);
        log.info("User created username={}, id={}", saved.getUsername(), saved.getUuid());
            return modelMapper.convertDTO(saved);


    }

    @Override
    public AuthEntityDTO updateUser(UUID id, UpdateUserRequest request) {
        log.debug("Updating user id={}", id);
        AuthEntity authEntity = authRepository.findByIdWithRoles(id)
                .orElseThrow(()->new RuntimeException("User not found with id: "+id));
        if(request.getEmail()!=null && !request.getEmail().equals(authEntity.getEmail())){
            if(authRepository.existsByEmail(request.getEmail())){
                throw new RuntimeException("Email already exists: "+request.getEmail());
            }
            authEntity.setEmail(request.getEmail());
        }
        if(request.getFirstName()!=null){
            authEntity.setFirstName(request.getFirstName());
        }
        if(request.getLastName()!=null){
            authEntity.setLastName(request.getLastName());
        }
        if(request.getEnabled()!=null){
            authEntity.setEnabled(request.getEnabled());
        }

        if(request.getRoleIds()!=null){
            Set<RoleEntity> newRoles = new HashSet<>(roleEntityRepository.findAllById(request.getRoleIds()));
            if(newRoles.size()!=request.getRoleIds().size()){
                throw new RuntimeException("Some roles were not found");
            }
            authEntity.getRoleEntities().clear();
            authEntity.setRoleEntities(newRoles);
        }
        AuthEntity entity = authRepository.save(authEntity);
        log.info("User updated username={}, id={}", entity.getUsername(), id);
        return modelMapper.convertDTO(entity);

    }
    public AuthEntityDTO getUserByUsername(String username){
        AuthEntity authEntity = authRepository.findByUsernameWithRolesAndPermission(username)
                .orElseThrow(()-> new RuntimeException("User not found: "+username));
        return modelMapper.convertDTO(authEntity);
    }

    @Override
    public void deleteUser(UUID id) {
        log.debug("Deleting user id={}", id);
        AuthEntity authEntity = authRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        authRepository.delete(authEntity);
        log.info("User deleted username={}, id={}", authEntity.getUsername(), id);

    }

    @Override
    public AuthEntityDTO assignRolesToUser(UUID id, Set<Long> roleIds) {
        log.debug("Assigning roles to user id={}, roleIds={}", id,roleIds);
        AuthEntity entity = authRepository.findByIdWithRoles(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        Set<RoleEntity> newRoles = new HashSet<>(roleEntityRepository.findAllById(roleIds));
        if (newRoles.size() != roleIds.size()) {
            throw new RuntimeException("Some roles were not found");
        }
        entity.getRoleEntities().addAll(newRoles);
        AuthEntity authEntity = authRepository.save(entity);
        return modelMapper.convertDTO(authEntity);

    }

    @Override
    public AuthEntityDTO removeRolesFromUser(UUID id, Set<Long> roleIds) {
        log.debug("Removing roles from user id={}",id);
        AuthEntity authEntity = authRepository.findByIdWithRoles(id).orElseThrow(() -> new RuntimeException("User not found with id:"));
        Set<RoleEntity> rolesRemove = new HashSet<>(roleEntityRepository.findAllById(roleIds));
        authEntity.getRoleEntities().removeAll(rolesRemove);
        AuthEntity updatedUser = authRepository.save(authEntity);
        return modelMapper.convertDTO(updatedUser);

    }

    @Override
    public Set<PermissionDTO> getUserPermissions(UUID id) {
        log.debug("Fetching permissions for user id={}", id);
        AuthEntity authEntity = authRepository.findByIdWithRoles(id).orElseThrow(() -> new RuntimeException("User not found with id:"));
        return authEntity.getAllPermission().stream()
                .map(modelMapper::convertPermissionToDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean userHasPermission(UUID id, String permissionName) {
        log.debug("Checking if user has permission id={}, permissionName={}", id, permissionName);
        AuthEntity authEntity = authRepository.findByIdWithRoles(id).orElseThrow(() -> new RuntimeException("User not found with id:"));
        return authEntity.hasPermission(permissionName);

    }
}
