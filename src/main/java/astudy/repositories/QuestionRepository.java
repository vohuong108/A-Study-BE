package astudy.repositories;

import astudy.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(
            value = "SELECT ID, score FROM question q WHERE q.quiz_id = :quizId",
            nativeQuery = true)
    List<Long[]> getListIdByQuizId(@Param("quizId") Long quizId);
}
