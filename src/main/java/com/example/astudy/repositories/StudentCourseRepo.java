package com.example.astudy.repositories;

import com.example.astudy.entities.StudentCourse;
import com.example.astudy.entities.key.StudentCourseKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCourseRepo extends JpaRepository<StudentCourse, StudentCourseKey> {
    @Query(
            nativeQuery = true,
            value = "SELECT * \n" +
                    "FROM student_course sc \n" +
                    "WHERE sc.student_id = (SELECT s.id AS student_id FROM app_user s WHERE s.username = :student_username)\n" +
                    "ORDER BY enrolled_at ASC"
    )
    List<StudentCourse> findEnrolledCourseByStudentUsernameOrderByEnrolledAt(
            @Param("student_username") String studentUsername
    );

    @Query(
            value = "SELECT CASE WHEN COUNT(*) = 1 THEN 'true' ELSE 'false' END\n" +
                    "FROM student_course sc \n" +
                    "WHERE sc.student_id = (SELECT id FROM app_user WHERE app_user.username = :studentName)\n" +
                    "AND sc.course_id = :courseId",
            nativeQuery = true
    )
    boolean checkEnrolledByCourseIdAndStudentName(
            @Param("courseId") Long courseId,
            @Param("studentName") String studentName
    );
}
