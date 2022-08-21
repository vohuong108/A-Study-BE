package com.example.astudy.repositories;

import com.example.astudy.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizRepo extends JpaRepository<Quiz, Long> {

    @Query(
            value = "SELECT attempt_allow FROM quiz q WHERE q.id = :id",
            nativeQuery = true
    )
    int findAttemptAllowByQuizId(@Param("id") Long id);
}
