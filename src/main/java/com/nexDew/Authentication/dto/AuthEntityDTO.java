package com.nexDew.Authentication.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthEntityDTO {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Set<RoleDTO> roles;
    private Set<PermissionDTO> permission;
    private Boolean enabled;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
