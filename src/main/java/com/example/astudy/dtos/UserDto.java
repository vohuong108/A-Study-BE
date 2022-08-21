package com.example.astudy.dtos;

import com.example.astudy.enums.AccountStatus;
import com.example.astudy.enums.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long ID;
    private String username;
    private String password;
    private String email;
    private AppUserRole userRole;
    private AccountStatus status;

    private UserProfileDto profile;
}
