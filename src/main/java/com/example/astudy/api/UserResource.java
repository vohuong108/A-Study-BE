package com.example.astudy.api;

import com.example.astudy.config.security.utils.SecurityUtils;
import com.example.astudy.dtos.PasswordDto;
import com.example.astudy.dtos.ResponseBodyInfo;
import com.example.astudy.dtos.UserDto;
import com.example.astudy.entities.Role;
import com.example.astudy.entities.UserProfile;
import com.example.astudy.services.UserService;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static com.example.astudy.constant.SecurityConstants.API_V1_USER_ROOT_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V1_USER_ROOT_URL)
public class UserResource {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<?> getUserProfile(){
        String username = SecurityUtils.getAuthenticatedUsername();

        return ResponseEntity.ok().body(userService.getUser(username));
    }


    @PutMapping("/changeinfo")
    public ResponseEntity<?> changeInfo(@RequestBody UserProfile userProfile) {
        String username = SecurityUtils.getAuthenticatedUsername();

        return ResponseEntity.ok().body(userService.updateProfile(username, userProfile));
    }

    @PutMapping("/changpass")
    public ResponseEntity<?> changePassword(
            @NonNull @RequestBody PasswordDto passwordDto,
            HttpServletRequest request
    ) {
        String username = SecurityUtils.getAuthenticatedUsername();

        userService.updatePassword(username, passwordDto);

        return ResponseEntity.ok().body(new ResponseBodyInfo(
                request.getRequestURI(),
                "Update password successfully"
        ));

    }

    @GetMapping("/username")
    public ResponseEntity<UserDto> getUserByUsername(@RequestBody UserForm userForm) {
        return ResponseEntity.ok().body(userService.getUser(userForm.getUsername()));
    }

    @PostMapping("/save")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }
}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}

@Data
class UserForm {
    private String username;
}

