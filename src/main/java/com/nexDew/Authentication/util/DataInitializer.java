package com.nexDew.Authentication.util;

import com.nexDew.Authentication.dto.AdminProperties;
import com.nexDew.Authentication.entity.AuthEntity;
import com.nexDew.Authentication.entity.Permission;
import com.nexDew.Authentication.entity.RoleEntity;
import com.nexDew.Authentication.entity.type.ActionType;
import com.nexDew.Authentication.entity.type.ResourceType;
import com.nexDew.Authentication.repository.AuthRepository;
import com.nexDew.Authentication.repository.PermissionRepository;
import com.nexDew.Authentication.repository.RoleEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class DataInitializer implements CommandLineRunner {
    private final AuthRepository authRepository;

    private final PermissionRepository permissionRepository;
    private final RoleEntityRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing default data....");
        // Create Permission
        Set<Permission> permissions = createPermissions();

        // Create Roles
        Set<RoleEntity> roles = createRoles(permissions);

        // Create Admin User

        createAdminUser(roles);
        log.info("Data Initialization Completed successfully!");
    }

    private Set<Permission> createPermissions() {
        Set<Permission> permissions = new HashSet<>();

        // USER permissions
        permissions.add(createdPermissionIfNotExists("USER_READ", "Read user data", ResourceType.USER, ActionType.READ));
        permissions.add(createdPermissionIfNotExists("USER_WRITE", "Create/Update user data", ResourceType.USER, ActionType.WRITE));
        permissions.add(createdPermissionIfNotExists("USER_DELETE", "Delete user data", ResourceType.USER, ActionType.DELETE));

        // ROLE permissions
        permissions.add(createdPermissionIfNotExists("ROLE_READ", "Read roles", ResourceType.ROLE, ActionType.READ));
        permissions.add(createdPermissionIfNotExists("ROLE_WRITE", "Create/Update roles", ResourceType.ROLE, ActionType.WRITE));
        permissions.add(createdPermissionIfNotExists("ROLE_DELETE", "Delete roles", ResourceType.ROLE, ActionType.DELETE));

        // PERMISSION permissions
        permissions.add(createdPermissionIfNotExists("PERMISSION_READ", "Read permissions", ResourceType.PERMISSION, ActionType.READ));
        permissions.add(createdPermissionIfNotExists("PERMISSION_WRITE", "Create/Update permissions", ResourceType.PERMISSION, ActionType.WRITE));
        permissions.add(createdPermissionIfNotExists("PERMISSION_DELETE", "Delete permissions", ResourceType.PERMISSION, ActionType.DELETE));

        // REPORT permissions
        permissions.add(createdPermissionIfNotExists("REPORT_READ", "Read reports", ResourceType.REPORT, ActionType.READ));
        permissions.add(createdPermissionIfNotExists("REPORT_WRITE", "Generate reports", ResourceType.REPORT, ActionType.WRITE));
        permissions.add(createdPermissionIfNotExists("REPORT_DELETE", "Delete reports", ResourceType.REPORT, ActionType.DELETE));
        permissions.add(createdPermissionIfNotExists("REPORT_EXECUTE", "Execute report jobs", ResourceType.REPORT, ActionType.EXECUTE));

        log.info("Created {} permissions", permissions.size());
        return permissions;
    }

    private Permission createdPermissionIfNotExists(
            String name,
            String description,
            ResourceType resourceType,
            ActionType actionType
    ) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> {
                    log.info("Creating permission: {}", name);
                    return permissionRepository.save(
                            Permission.builder()
                                    .name(name)
                                    .description(description)
                                    .resourceType(resourceType)
                                    .actionType(actionType)
                                    .active(true)
                                    .build()
                    );
                });
    }

    private Set<RoleEntity> createRoles(Set<Permission> allPermissions) {
        Set<RoleEntity> roles = new HashSet<>();
        // ADMIN-Role has All Permissions
        RoleEntity adminRole = createRoleIfNotExist(
                "ROLE_ADMIN",
                "Administrator with full access",
                allPermissions
        );
        roles.add(adminRole);

        // Manger Role - has Read / Write Access to most Resource

        Set<Permission> managerPermissions = new HashSet<>();
        allPermissions.stream()
                .filter(p -> p.getName().contains("READ") ||
                        p.getName().contains("WRITE") ||
                        p.getName().contains("EXECUTE"))
                .forEach(managerPermissions::add);

        RoleEntity managerRole = createRoleIfNotExist(
                "ROLE_MANAGER",
                "Manager with read and Write access",
                managerPermissions
        );
        roles.add(managerRole);

        // User Role - has Basic Read Access

        Set<Permission> userPermission = new HashSet<>();
        allPermissions.stream()
                .filter(p -> p.getName().equals("USER_READ"))
                .forEach(userPermission::add);
        RoleEntity userRole = createRoleIfNotExist(
                "ROLE_USER",
                "Regular user with basic access",
                userPermission
        );
        roles.add(userRole);
        log.info("Created {} roles", roles.size());
        return roles;
    }

    private RoleEntity createRoleIfNotExist(String name, String description, Set<Permission> permissions) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    RoleEntity roles = RoleEntity.builder()
                            .name(name)
                            .description(description)
                            .permissions(permissions)
                            .active(true)
                            .build();
                    return roleRepository.save(roles);
                });
    }

    private void createAdminUser(Set<RoleEntity> roles) {

        String username = adminProperties.getUsername();

        // Skip if already exists
        if (authRepository.findByUsername(username).isPresent()) {
            log.info("Admin user already exists. Skipping creation.");
            return;
        }

        RoleEntity adminRole = roles.stream()
                .filter(r -> r.getName().equals("ROLE_ADMIN"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        AuthEntity admin = AuthEntity.builder()
                .username(username)
                .email(adminProperties.getEmail())
                .password(passwordEncoder.encode(adminProperties.getPassword()))
                .roleEntities(Set.of(adminRole))
                .build();

        authRepository.save(admin);

        log.info("Admin user created: {}", username);
        log.warn("⚠️ Change admin password after first login!");

    }
}

