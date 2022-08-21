package com.example.astudy.services;

import com.example.astudy.dtos.PasswordDto;
import com.example.astudy.dtos.UserDto;
import com.example.astudy.entities.Role;
import com.example.astudy.entities.UserProfile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto user);
    UserDto getUser(String username);
    List<UserDto> getUsers();
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    Role saveRole(Role role);
    UserProfile saveProfile(UserProfile userProfile);
    UserProfile updateProfile(String username, UserProfile userProfile);
    void updatePassword(String username, PasswordDto passwordDto);

}
