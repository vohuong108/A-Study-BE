package com.example.astudy.repositories;

import com.example.astudy.entities.WeekContent;
import com.example.astudy.entities.projections.WeekContentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Collection;

@Repository
public interface WeekContentRepo extends JpaRepository<WeekContent, Long> {
    WeekContent findWeekContentByID(Long id);

    @Query(
            value = "SELECT id, content_order, content_status, content_type, name " +
                    "FROM week_content wc " +
                    "WHERE wc.week_id = :weekId AND wc.is_active = 1",
            nativeQuery = true
    )
    Collection<WeekContentProjection> findAllByWeekIdUseProjection(@Param("weekId") Long weekId);

    @Query(
            value = "SELECT id, content_order, content_status, content_type, name " +
                    "FROM week_content wc " +
                    "WHERE wc.week_id = :weekId AND wc.is_active = 1 AND wc.content_status = 'PUBLIC'",
            nativeQuery = true
    )
    Collection<WeekContentProjection> findWeekContentByWeekIdUseProjection(@Param("weekId") Long weekId);

    @Query(
            value = "SELECT week_id FROM week_content wc WHERE wc.id = :id AND wc.is_active = 1",
            nativeQuery = true
    )
    Long findWeekIdByWeekContentId(@Param("id") Long id);

    @Query(
            value = "SELECT course_id FROM week w WHERE w.id = (" +
                    "SELECT week_id FROM week_content wc " +
                    "WHERE wc.id = :quizId AND wc.content_type = 'QUIZ' AND wc.is_active = 1)",
            nativeQuery = true
    )
    Long findCourseIdByQuizId(@Param("quizId") Long quizId);

    @Query(
            value = "SELECT content_status FROM week_content wc " +
                    "WHERE wc.id = :id AND wc.is_active = 1",
            nativeQuery = true
    )
    String findContentStatusByContentId(@Param("id") Long id);
}
