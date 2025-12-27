package com.nexDew.Authentication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {
    private Long id;

    @NotBlank(message = "Role name is required")
    @Size(max = 50, message = "Role name must be less tha 50 characters")
    private String name;

    @Size(max = 500, message = "Description must be less than 500 character")
    private String description;

    private Set<PermissionDTO> permissions;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
