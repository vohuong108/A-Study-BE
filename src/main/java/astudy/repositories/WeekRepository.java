package astudy.repositories;


import astudy.models.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeekRepository extends JpaRepository<Week, Long> {
    @Query(
            value = "SELECT * FROM week w WHERE w.course_id = :courseId",
            nativeQuery = true)
    List<Week> findListWeekByCourseId(@Param("courseId") Long courseId);

    @Query(
            value = "SELECT ID FROM week w WHERE w.course_id = :courseId",
            nativeQuery = true)
    List<Long> getListIdByCourseId(@Param("courseId") Long courseId);
}
