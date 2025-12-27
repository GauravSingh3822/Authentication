package com.nexDew.Authentication.dto;

import com.nexDew.Authentication.entity.type.ActionType;
import com.nexDew.Authentication.entity.type.ResourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PermissionDTO {
    private Long id;

    @NotBlank(message = "Permission name is required")
    @Size(max = 100, message = "Permission must be less than 100 characters")
    private String name;

    @Size(max = 500, message = "Description must be less than 100 characters")
    private String description;

    @Size(max = 50, message = "Resource Type must be less than 100 characters")
    private ResourceType resourceType;

    @Size(max = 50, message = "Action Type must be less than 100 characters")
    private ActionType actionType;

    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
