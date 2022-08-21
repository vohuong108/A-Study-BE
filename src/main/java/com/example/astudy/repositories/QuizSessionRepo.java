package com.example.astudy.repositories;

import com.example.astudy.entities.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizSessionRepo extends JpaRepository<QuizSession, Long> {


    @Query(
            "SELECT qs FROM quiz_session qs " +
            "JOIN FETCH qs.quiz qz " +
            "JOIN FETCH qs.user u " +
            "JOIN FETCH qz.questions qzq " +
            "WHERE qs.sessionActive = true AND qs.sessionId = :id"
    )
    QuizSession findQuizSessionActiveBySessionId(@Param("id") Long id);
    QuizSession findQuizSessionBySessionId(Long sessionId);

    @Query(
            value = "SELECT session_id, session_active, start_time, time, quiz_id, user_id\n" +
                    "FROM quiz_session qs\n" +
                    "WHERE qs.quiz_id = :quizId AND qs.user_id = :userId AND qs.session_active = 1",
            nativeQuery = true
    )
    QuizSession findQuizSessionOperating(@Param("quizId") Long quizId, @Param("userId") Long userId);

    @Query(
            value = "SELECT COUNT(qs.session_id) as result " +
                    "FROM quiz_session qs " +
                    "WHERE qs.quiz_id = :quizId AND qs.user_id = :userId",
            nativeQuery = true
    )
    int countQuizSessionByQuizIdAndUserId(@Param("quizId") Long quizId, @Param("userId") Long userId);
}
