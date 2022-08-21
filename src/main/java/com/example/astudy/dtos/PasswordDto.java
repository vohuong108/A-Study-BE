package com.example.astudy.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordDto {
    private String password;
    private String newPass;
}
