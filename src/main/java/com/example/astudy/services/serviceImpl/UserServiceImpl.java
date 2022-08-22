package com.example.astudy.services.serviceImpl;

import com.example.astudy.dtos.AdminActionUserDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final int USER_PAGE_SIZE = 25;
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
    public List<UserDto> getAllUserInSystem(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, USER_PAGE_SIZE, Sort.by("ID"));
        Page<AppUser> users = userRepo.findAll(pageable);
        return users.stream().map(userMapper::appUserToUserDtoForAdmin).collect(Collectors.toList());
    }

    @Override
    public AdminActionUserDto changeStatusOfUser(AdminActionUserDto input) {
        Long userId = input.getUserId();
        if (userId == null) {
            throw new RequestFieldNotFoundException("User id must not null");
        }

        AccountStatus status = input.getAccountStatus();
        if (status == null) {
            throw new RequestFieldNotFoundException("status must not null");
        }

        AppUser user = userRepo.findAppUserByID(userId);

        if (user == null) {
            throw new RequestFieldNotFoundException(
                    String.format("User with id {%s} not found", userId)
            );
        }

        user.setStatus(status);

        AdminActionUserDto response = new AdminActionUserDto();
        response.setUserId(userId);
        response.setAccountStatus(status);

        return response;
    }

    @Override
    public AdminActionUserDto changeRoleOfUser(AdminActionUserDto input) {
        Long userId = input.getUserId();
        AppUserRole roleIn = input.getRole();

        if (userId == null) {
            throw new RequestFieldNotFoundException("User id must not null");
        }

        if (roleIn == null) {
            throw new RequestFieldNotFoundException("Role must not null");
        }

        AppUser user = userRepo.findAppUserByID(userId);

        if (user == null) {
            throw new RequestFieldNotFoundException(
                    String.format("User with id {%s} not found", userId)
            );
        }

        Role role = roleRepo.findByName(roleIn);

        if (role == null) {
            throw new RequestFieldNotFoundException(
                    String.format("Role {%s} invalid", roleIn)
            );
        }

        user.setRole(role);

        AdminActionUserDto response = new AdminActionUserDto();
        response.setUserId(userId);
        response.setRole(roleIn);

        return response;
    }
}
