package com.example.astudy.services.serviceImpl;

import com.example.astudy.dtos.PasswordDto;
import com.example.astudy.dtos.UserDto;
import com.example.astudy.dtos.mapper.UserMapper;
import com.example.astudy.entities.AppUser;
import com.example.astudy.entities.Role;
import com.example.astudy.entities.UserProfile;
import com.example.astudy.enums.AccountStatus;
import com.example.astudy.enums.AppUserRole;
import com.example.astudy.exceptions.RequestFieldNotFoundException;
import com.example.astudy.exceptions.SignUpException;
import com.example.astudy.exceptions.UpdateUserInfoException;
import com.example.astudy.repositories.ProfileRepo;
import com.example.astudy.repositories.RoleRepo;
import com.example.astudy.repositories.UserRepo;
import com.example.astudy.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final ProfileRepo profileRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username);

        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException(String.format("User %s not found in the database", username));
        } else {
            log.info("User found in the database: {}", username);
        }

        Collection<SimpleGrantedAuthority> authorities = user.getRole().getName().getGrantedAuthorities();
        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public UserProfile saveProfile(UserProfile userProfile) {
        return profileRepo.save(userProfile);
    }

    @Override
    public UserProfile updateProfile(String username, UserProfile userProfile) {
        AppUser user = userRepo.findByUsername(username);

        UserProfile currentlyProfile = user.getProfile();
        if(currentlyProfile != null) {
            currentlyProfile.update(userProfile);
            return currentlyProfile;

        } else {
            UserProfile createdProfile = profileRepo.save(userProfile);
            user.setProfile(createdProfile);
            return createdProfile;
        }
    }

    @Override
    public void updatePassword(String username, PasswordDto passwordDto) {
        AppUser user = userRepo.findByUsername(username);

        if (user == null) {
            throw new RequestFieldNotFoundException("Update password failed. Username not found");
        }

        if (passwordEncoder.matches(passwordDto.getPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordDto.getNewPass()));
        } else throw new UpdateUserInfoException("Password incorrect");
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        if (userRepo.existsAppUserByUsernameOrEmailAndStatus(
                userDto.getUsername(),
                userDto.getEmail(),
                AccountStatus.ACTIVE
        )) {
            throw new SignUpException("Failed, Username or Email exist !!!");

        } else {
            AppUser user = userMapper.userDtoToAppUser(userDto);

            UserProfile userProfile = null;

            if (userDto.getProfile() != null) {
                userProfile = this.saveProfile(
                        userMapper.userProfileDtoToUserProfile(userDto.getProfile())
                );
            }

            AppUserRole appUserRole = userDto.getUserRole();
            Role role = roleRepo.findByName(appUserRole);

            //TODO: if role not exist, i can create role
            if (role == null) {
                throw new RequestFieldNotFoundException(String.format("Role %s not found !!!", appUserRole.name()));
            }

            user.setProfile(userProfile);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setRole(role);

            //TODO: handle Sql exception
            AppUser createdUser = userRepo.save(user);
            return userMapper.appUserToUserDto(createdUser);
        }

    }

    @Override
    public UserDto getUser(String username) {
        AppUser user = userRepo.findByUsername(username);

        if (user == null) throw new RequestFieldNotFoundException(String.format("Username %s not found!!!", username));
        return userMapper.appUserToUserDto(user);
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepo.findAll().stream().map(userMapper::appUserToUserDto).collect(Collectors.toList());
    }
}
