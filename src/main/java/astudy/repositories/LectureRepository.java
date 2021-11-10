package astudy.repositories;

import astudy.models.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @Query(
            value = "SELECT * FROM lecture l WHERE l.ID = :lecId",
            nativeQuery = true)
    Lecture findByWeekIdAndLectureId(@Param("lecId") Long lecId);
}
