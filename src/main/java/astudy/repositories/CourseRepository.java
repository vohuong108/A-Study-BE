package astudy.repositories;

import astudy.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "SELECT * FROM course c WHERE c.ID = :courseId", nativeQuery = true)
    Course findCourseById(@Param("courseId") Long courseId);

    @Query(value = "SELECT ID, description, learn_info, name, release_date, skill_info, author_id " +
            "FROM course c WHERE c.ID = :courseId", nativeQuery = true)
    String[][] customFindCourseById(@Param("courseId") Long courseId);

    @Query(value = "SELECT ID, name, author_id " +
            "FROM course WHERE MATCH(name) AGAINST(:query IN NATURAL LANGUAGE MODE)", nativeQuery = true)
    String[][] searchCourseByQueryName(@Param("query") String query);
}
