package com.example.astudy.repositories;

import com.example.astudy.entities.Submit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmitRepo extends JpaRepository<Submit, Long> {
}
