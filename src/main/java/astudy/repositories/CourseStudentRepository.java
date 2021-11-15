package astudy.repositories;

import astudy.models.CourseStudent;
import astudy.models.CourseStudentKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CourseStudentRepository extends JpaRepository<CourseStudent, CourseStudentKey> {
    @Query(value = "SELECT IF(student_id, TRUE, FALSE) " +
            "FROM course_student cs " +
            "WHERE cs.student_id = :studentId AND cs.course_id = :courseId", nativeQuery = true)
    Integer checkEnrolledStudent(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId);


    @Query(value = "INSERT INTO course_student (course_id, student_id, enrolled_at) " +
            "VALUES (:courseId, :studentId, :releaseDate)", nativeQuery = true)
    void insertCourseStudent(
            @Param("studentId") Long studentId,
            @Param("courseId") Long courseId,
            @Param("releaseDate") Date releaseDate);

}
