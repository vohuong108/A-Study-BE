package com.example.astudy.repositories;

import com.example.astudy.entities.AppUser;
import com.example.astudy.enums.AccountStatus;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);

    @Query(
            value = "SELECT id FROM app_user u WHERE u.username = :username",
            nativeQuery = true
    )
    Long findUserIdByUsername(@Param("username") String username);

    Boolean existsAppUserByUsernameOrEmailAndStatus(
            @NonNull String username,
            @NonNull String email,
            @NonNull AccountStatus status);
}