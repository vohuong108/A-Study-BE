package com.example.astudy.dtos;

import com.example.astudy.enums.AccountStatus;
import com.example.astudy.enums.AppUserRole;
import lombok.Data;

@Data
public class AdminActionUserDto {
    private Long userId;
    private AccountStatus accountStatus;
    private AppUserRole role;
}
