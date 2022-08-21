package com.example.astudy.repositories;

import com.example.astudy.entities.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WeekRepo extends JpaRepository<Week, Long> {
    @Query(
            nativeQuery = true,
            value = "SELECT id, name, week_order, course_id FROM week w WHERE w.id=:weekId AND w.course_id=:courseId"
    )
    Week findWeekByWeekIDAndCourseID(@Param("weekId") Long weekId, @Param("courseId") Long courseId);
}
