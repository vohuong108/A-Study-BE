package com.example.astudy.dtos.mapper;

import com.example.astudy.dtos.UserDto;
import com.example.astudy.dtos.UserProfileDto;
import com.example.astudy.entities.AppUser;
import com.example.astudy.entities.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
        @Mapping(target = "role", ignore = true),
        @Mapping(target = "profile", ignore = true)
    })
    AppUser userDtoToAppUser(UserDto userDto);

    @Mappings({
        @Mapping(target = "password", ignore = true),
        @Mapping(target = "userRole", expression = "java(appUser.getRole().getName())"),
        @Mapping(target = "profile", source = "profile")
    })
    UserDto appUserToUserDto(AppUser appUser);

    UserProfileDto userProfileToUserProfileDto(UserProfile userProfile);

    UserProfile userProfileDtoToUserProfile(UserProfileDto userProfileDto);

    List<AppUser> listUserDtoToListAppUser(List<UserDto> userDtoList);

    List<UserDto> listAppUserToListUserDto(List<AppUser> appUserList);
}
