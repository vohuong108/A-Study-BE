package astudy.repositories;

import astudy.models.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Query(
            value = "SELECT ID FROM quiz WHERE quiz.week_id = :weekId",
            nativeQuery = true)
    List<Long> getListIdByWeekId(@Param("weekId") Long weekId);
}
