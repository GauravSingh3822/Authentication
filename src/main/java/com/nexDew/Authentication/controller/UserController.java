package com.nexDew.Authentication.controller;

import com.nexDew.Authentication.dto.*;
import com.nexDew.Authentication.error.APIResponse;
import com.nexDew.Authentication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    // 1. Get All Users
    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<?> getAllUsers(){
        try{
            List<AuthEntityDTO> authEntityDTO = userService.getAllUsers();
            return new ResponseEntity<>(APIResponse.success("Users fetched successfully", authEntityDTO), HttpStatus.OK);

        } catch(Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 2. Get User By ID
    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('USER_READ') or #id==authentication.principal.id")
    public ResponseEntity<?> getUserById(@PathVariable UUID uuid){
        try{
            AuthEntityDTO authEntityDTO = userService.getUserById(uuid);
            return new ResponseEntity<>(APIResponse.success("User fetched successfully", authEntityDTO), HttpStatus.OK);

        } catch(Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 3. Create User
    @PostMapping
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request){
        try{
            AuthEntityDTO authEntity = userService.createUser(request);
            return new ResponseEntity<>(APIResponse.success("User created successfully", authEntity), HttpStatus.CREATED);

        }catch (Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }

    }
    // 4. Update User
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_WRITE') or #id==authentication.principal.id")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request){
        try{
            AuthEntityDTO authEntityDTO = userService.updateUser(id,request);
            return new ResponseEntity<>(APIResponse.success("User updated successfully", authEntityDTO), HttpStatus.OK);
        }catch(Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // 5. Delete User
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id){
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(APIResponse.success("User deleted successfully", null), HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // 6. Assign Role
    @PostMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<?> assignRoleToUser(@PathVariable UUID id, @RequestBody Set<Long> roleIds){
        try{
            AuthEntityDTO authEntityDTO = userService.assignRolesToUser(id,roleIds);
            return new ResponseEntity<>(APIResponse.success("Role assigned successfully", authEntityDTO), HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // 7. Remove Role
    @DeleteMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable UUID id, @RequestBody Set<Long> roleIds){
        try{
            AuthEntityDTO authEntityDTO = userService.removeRolesFromUser(id,roleIds);
            return new ResponseEntity<>(APIResponse.success("Role removed successfully", authEntityDTO), HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    // 8. Get user's permission
    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('USER_READ') or #id==authentication.principal.id")
    public ResponseEntity<?> getUserPermissions(@PathVariable UUID id){
        try{
            Set<PermissionDTO> permissionDTO = userService.getUserPermissions(id);
            return new ResponseEntity<>(APIResponse.success("Permission fetched successfully", permissionDTO), HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    // 9. Check if user has permission
    @GetMapping("/{id}/has-permissions/{permissionName}")
    @PreAuthorize("hasPermission(#id,'User','USER_READ'}")
    public ResponseEntity<?> checkIfUserHasPermission(@PathVariable UUID id, @PathVariable String permissionName){
        try{
            boolean hasPermission = userService.userHasPermission(id,permissionName);
            return new ResponseEntity<>(APIResponse.success("Permission check successfully", hasPermission), HttpStatus.OK);
        }catch(Exception exception){
            return new ResponseEntity<>(APIResponse.error(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


}
