package com.example.astudy.dtos;

import lombok.Data;

@Data
public class UserProfileDto {
    private Long ID;
    private String phone;
    private String address;
    private byte[] avatar;
    private String firstName;
    private String lastName;
}
