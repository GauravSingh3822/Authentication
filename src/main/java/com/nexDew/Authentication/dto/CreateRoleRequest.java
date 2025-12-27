package com.nexDew.Authentication.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class CreateRoleRequest {
    @NotBlank(message = "Role name is required")
    private String name;
    private String description;
    private Set<Long> permissionIds;
}
