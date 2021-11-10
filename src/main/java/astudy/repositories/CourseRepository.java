package astudy.repositories;

import astudy.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(
            value = "SELECT * FROM course c WHERE c.ID = :courseId",
            nativeQuery = true)
    Course findCourseById(@Param("courseId") Long courseId);
}
