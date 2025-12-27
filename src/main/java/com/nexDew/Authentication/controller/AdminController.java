package com.nexDew.Authentication.controller;

import com.nexDew.Authentication.dto.CreateRoleRequest;
import com.nexDew.Authentication.dto.PermissionDTO;
import com.nexDew.Authentication.dto.RoleDTO;
import com.nexDew.Authentication.dto.UpdateRoleRequest;
import com.nexDew.Authentication.error.APIResponse;
import com.nexDew.Authentication.service.PermissionService;
import com.nexDew.Authentication.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final PermissionService permissionService;
    private final RoleService roleService;

//=======================Role Management Endpoints====================================
    // Get All Role
    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles(){
        try{
            List<RoleDTO> roles = roleService.getAllRoles();
            return new ResponseEntity<>(APIResponse.success("Roles retrived successfully", roles),HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Get Role By Id
    @GetMapping("/roles/{id}")
    public ResponseEntity<?> getRolesById(@PathVariable Long id){
        try {
            RoleDTO roleDTO = roleService.getRoleById(id);
            return new ResponseEntity<>(APIResponse.success("Roles retrived succesfully", roleDTO),HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    // Create Role
    @PostMapping("/roles")
    public ResponseEntity<?> createRole(@Valid @RequestBody CreateRoleRequest request){
        try {
            RoleDTO roleDTO = roleService.createRole(request);
            return new ResponseEntity<>(APIResponse.success("Roles retrived succesfully", roleDTO),HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // Update Role
    @PutMapping("/roles/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Long id,@Valid @RequestBody UpdateRoleRequest request){
        try{
            RoleDTO role = roleService.updateRole(id,request);
            return new ResponseEntity<>(APIResponse.success("Role update successfully", role),HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // Delete Role
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id){
        try{
            roleService.deleteRole(id);
            return new ResponseEntity<>(APIResponse.success("Role deleted successfully", null),HttpStatus.OK);

        }catch (Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // Assign Permission To Role
    @PostMapping("/roles/{roleId}/permissions")
        public ResponseEntity<?> assignPermissionToRole(@PathVariable Long roleId, @RequestBody Set<Long> permissionIds){
        try {
            RoleDTO roleDTO = roleService.assignPermissionToRole(roleId,permissionIds);
            return new ResponseEntity<>(APIResponse.success("Permission assigned successfully", roleDTO),HttpStatus.OK);

        }catch (Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // Remove Permission From Role
    @DeleteMapping("/roles/{roleId}/permissions")
    public ResponseEntity<?> removePermissionToRole(@PathVariable Long roleId,@RequestBody Set<Long> permissionIds){
        try{
            RoleDTO roleDTO = roleService.removePermissionFromRole(roleId,permissionIds);
            return new ResponseEntity<>(APIResponse.success("Permission removed successfully", roleDTO),HttpStatus.OK);

        }catch(Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    // Get Role Permission
    @GetMapping("/roles/{roleId}/permission")
    public ResponseEntity<?> getRolePermission(@PathVariable Long roleId){
        try{
            Set<PermissionDTO> permissionDTOS = roleService.getRolePermissions(roleId);
            return new ResponseEntity<>(APIResponse.success("Permission fetched successfully", permissionDTOS),HttpStatus.OK);

        }catch(Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


//=======================Permission Management Endpoints====================================
    // Get All Permission
    @GetMapping("/permissions")
    public ResponseEntity<?> getAllPermission(){
        try {
            List<PermissionDTO> allPermission = permissionService.getAllPermission();
            return ResponseEntity.ok(APIResponse.success("Permission fetched successfully",allPermission));
        }catch(Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(exception.getMessage()));

        }
    }
    // Get Permission By Id
    @GetMapping("/permissions/{id}")
    public ResponseEntity<?> getPermissionNyId(@PathVariable Long id){
        try{
            PermissionDTO permissionDTO = permissionService.getPermissionById(id);
            return ResponseEntity.ok(APIResponse.success("Permission fetched successfully",permissionDTO));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(exception.getMessage()));

        }
    }
    // Get Permission By Name
    @GetMapping("/permissions/resource/{resourceType}")
    public ResponseEntity<?> getPermissionByResourceType(@PathVariable String resourceType){
        try{
            List<PermissionDTO> permissionDTOS = permissionService.getPermissionByResourceType(resourceType);
            return ResponseEntity.ok(APIResponse.success("Permission fetched successfully",permissionDTOS));

        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(exception.getMessage()));
        }
    }
    // Create Permission
    @PostMapping("/permissions")
    public ResponseEntity<?> createPermission(@Valid  @RequestBody PermissionDTO permissionDTO){
        try{
            PermissionDTO permission = permissionService.createPermission(permissionDTO);
            return ResponseEntity.ok(APIResponse.success("Permission fetched successfully",permission));

        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(exception.getMessage()));
        }
    }
    // Update Permission
    @PutMapping("/permissions/{id}")
    public ResponseEntity<?> updatePermission(@PathVariable Long id,@Valid @RequestBody PermissionDTO permissionDTO){
        try{
            PermissionDTO permission = permissionService.updatePermission(id,permissionDTO);
            return ResponseEntity.ok(APIResponse.success("Permission update successfully",permission));
    }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(exception.getMessage()));
        }
    }
    // Delete Permission
    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable Long id){
        try{
            permissionService.deletePermission(id);
            return ResponseEntity.ok(APIResponse.success("Permission deleted successfully",null));

    }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(exception.getMessage()));
        }
    }

}
