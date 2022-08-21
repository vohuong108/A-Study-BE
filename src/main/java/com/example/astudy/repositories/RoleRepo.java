package com.example.astudy.repositories;

import com.example.astudy.entities.Role;
import com.example.astudy.enums.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(AppUserRole name);
}
