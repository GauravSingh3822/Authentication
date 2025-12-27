package com.nexDew.Authentication.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignPermissionRequest {

    @NotEmpty(message = "At least one Permission is required")
    private Set<Long> permissionIds;
}

