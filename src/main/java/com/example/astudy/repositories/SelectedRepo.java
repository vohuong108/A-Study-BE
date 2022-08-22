package com.example.astudy.repositories;

import com.example.astudy.entities.Selected;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectedRepo extends JpaRepository<Selected, Long> {
    List<Selected> findSelectedsBySubmitID(Long submitId);
}
