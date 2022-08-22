package com.example.astudy.repositories;

import com.example.astudy.entities.Submit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmitRepo extends JpaRepository<Submit, Long> {

    @Query(
            value = "SELECT id, finish_time, grade, start_time, state, quiz_id, student_id, quiz_session_id\n" +
                    "FROM submit s\n" +
                    "WHERE s.quiz_id = :quizId AND s.student_id = :studentId",
            nativeQuery = true
    )
    List<Submit> findSubmitsByQuizIDAndStudentID(
            @Param("quizId") Long quizId,
            @Param("studentId") Long studentId
    );

    @Query(
            value = "SELECT id, finish_time, grade, start_time, state, quiz_id, student_id, quiz_session_id\n" +
                    "FROM submit s\n" +
                    "WHERE s.quiz_id = :quizId AND s.id = :id",
            nativeQuery = true
    )
    Submit findSubmitsByQuizIDAndID(
            @Param("quizId") Long quizId,
            @Param("id") Long id
    );
}
