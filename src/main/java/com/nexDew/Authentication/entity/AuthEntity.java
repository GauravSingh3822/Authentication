package com.nexDew.Authentication.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthEntity implements UserDetails {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(unique = true, nullable = false, length = 100)
    private String username;


    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String email;
    private String phoneNumber;

    private String firstName;
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<RoleEntity> roleEntities = new HashSet<>();

    private Boolean enabled = true;

    private Boolean accountNonExpried = true;

    private Boolean accountNonLocked = true;

    private Boolean credentialsNonExpired = true;

    private LocalDateTime lastLogin;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void OnCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdated() {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities = new HashSet<>();

        // Roles
        roleEntities.forEach(role ->
                authorities.add((GrantedAuthority) () -> role.getName())
        );

        // Permissions
        roleEntities.forEach(role ->
                role.getPermissions().forEach(permission ->
                        authorities.add(
                                (GrantedAuthority) () -> permission.getName()
                        )
                )
        );

        return authorities;
    }

    // Get All permission from all roles
    public Set<Permission> getAllPermission() {
        return roleEntities.stream()
                .flatMap(roleEntity -> roleEntity.getPermissions().stream())
                .collect(Collectors.toSet());
    }

    // Check if User has a specific permission
    public boolean hasPermission(String permissionName) {
        return getAllPermission().stream().anyMatch(permission -> permission.getName().equals(permissionName)
                && permission.getActive());
    }

    // Check if User has any of the specific permission
    public boolean hasAnyPermission(String... permissionNames) {
        Set<String> userPermissions = getAllPermission().stream()
                .filter(Permission::getActive)
                .map(Permission::getName)
                .collect(Collectors.toSet());
        return Arrays.stream(permissionNames)
                .anyMatch(userPermissions::contains);
    }

    // Check if user has all of the given permission
    public boolean hasAllPermission(String... permissionNames) {
        Set<String> userPermissions = getAllPermission().stream()
                .filter(Permission::getActive)
                .map(Permission::getName)
                .collect(Collectors.toSet());
        return Arrays.stream(permissionNames)
                .allMatch(userPermissions::contains);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AuthEntity authEntity)) return false;
        return uuid != null && uuid.equals(authEntity.uuid);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpried;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}