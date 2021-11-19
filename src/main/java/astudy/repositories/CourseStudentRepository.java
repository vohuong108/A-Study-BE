package astudy.repositories;

import astudy.models.CourseStudent;
import astudy.models.CourseStudentKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    @Query(value = "SELECT ID " +
            "FROM course_student cs " +
            "WHERE cs.course_id = :courseId", nativeQuery = true)
    List<Long> findListIdByCourseId(@Param("courseId") Long courseId);

    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM course_student " +
                    "WHERE course_student.ID = :id",
            nativeQuery = true)
    void deleteById(@Param("id") Long id);

}
