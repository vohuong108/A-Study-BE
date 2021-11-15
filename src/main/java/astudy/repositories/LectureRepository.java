package astudy.repositories;

import astudy.models.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @Query(
            value = "SELECT * FROM lecture l WHERE l.ID = :lecId",
            nativeQuery = true)
    Lecture findByLectureId(@Param("lecId") Long lecId);

    @Query(
            value = "SELECT ID FROM lecture WHERE lecture.week_id = :weekId",
            nativeQuery = true)
    List<Long> getListIdByWeekId(@Param("weekId") Long weekId);
}
