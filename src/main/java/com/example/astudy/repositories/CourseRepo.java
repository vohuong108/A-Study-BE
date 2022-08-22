package com.example.astudy.repositories;

import com.example.astudy.entities.Course;
import com.example.astudy.entities.projections.SearchCourseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {
    Course findCourseByID(Long id);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM course c WHERE c.author_name = :author_name"
    )
    List<Course> findCoursesByAuthorName(@Param("author_name") String author_name);

    @Query(
            nativeQuery = true,
            value = "SELECT CASE WHEN COUNT(*) = 1 THEN 'true' ELSE 'false' END\n" +
                    "FROM course c\n" +
                    "WHERE c.id = :courseId"
    )
    boolean checkCourseExist(@Param("courseId") Long courseId);
    @Query(
            nativeQuery = true,
            value = "SELECT author_name FROM course c WHERE c.id = :courseId"
    )
    String findAuthorNameById(@Param("courseId") Long courseId);

    @Query(
            value = "SELECT id, name, author_name FROM course WHERE MATCH(name) AGAINST(:query)",
            nativeQuery = true
    )
    List<SearchCourseProjection> searchCourseByQuery(@Param("query") String query);

}
