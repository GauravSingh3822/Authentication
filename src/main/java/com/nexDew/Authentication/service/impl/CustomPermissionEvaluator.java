package com.nexDew.Authentication.service.impl;

import com.nexDew.Authentication.entity.AuthEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Slf4j
public class CustomPermissionEvaluator implements PermissionEvaluator {
    // Evaluate Permission on a specific domain object-- @PreAuthorize (:hasPermission(#user,'USER_WRITE))
    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomainObject,
                                 Object permission) {

        if(authentication ==null || !authentication.isAuthenticated()){
            return false;
        }
        String permissionString = permission.toString();
        // Check if user has the required permission
        if(authentication.getPrincipal() instanceof AuthEntity authEntity){
            boolean hasPermission = authEntity.hasPermission(permissionString);
            log.debug("Permission check for user '{}' on permission '{}' : {}",
                    authEntity.getUsername(),permissionString,hasPermission);
            return hasPermission;
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if(authentication == null || !authentication.isAuthenticated()){
            return false;
        }
        String permissionString = permission.toString();
        if(authentication.getPrincipal() instanceof AuthEntity authEntity) {
            boolean hasPermission = authEntity.hasPermission(permissionString);

            // Additional logic based on target type and ID can be added here
            if (hasPermission && targetType.equals("Users") && targetId != null) {
                if(authEntity.getUuid().equals(targetId)){
                    return true;
                }

            }
            log.debug("Permission check for User '{}' on {} [id={}] with permission '{}':{}",
                    authEntity.getUsername(),targetType,targetId,permissionString,hasPermission);
            return hasPermission;
        }

        return false;
    }

    // Check if User has Any of the specific permission
    public boolean hasAnyPermission(Authentication authentication,String... permissions){
        if(authentication ==null || !authentication.isAuthenticated()){
            return false;
        }
        if(authentication.getPrincipal() instanceof AuthEntity authEntity){
            return authEntity.hasAnyPermission(permissions);
        }
        return false;
    }
    // Check if User has All specific Permissions
    public boolean hasAllPermission(Authentication authentication,String... permissions){
        if(authentication ==null || !authentication.isAuthenticated()){
            return false;
        }
        if(authentication.getPrincipal() instanceof AuthEntity authEntity){
            return authEntity.hasAllPermission(permissions);
        }
        return false;
    }
}

