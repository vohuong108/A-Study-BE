package com.example.astudy.repositories;

import com.example.astudy.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepo extends JpaRepository<UserProfile, Long> {
}
