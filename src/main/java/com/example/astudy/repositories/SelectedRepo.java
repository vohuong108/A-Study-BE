package com.example.astudy.repositories;

import com.example.astudy.entities.Selected;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectedRepo extends JpaRepository<Selected, Long> {
}
