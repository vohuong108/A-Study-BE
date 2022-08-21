package com.example.astudy.repositories;

import com.example.astudy.entities.AppUser;
import com.example.astudy.enums.AccountStatus;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);

    Boolean existsAppUserByUsernameOrEmailAndStatus(
            @NonNull String username,
            @NonNull String email,
            @NonNull AccountStatus status);
}