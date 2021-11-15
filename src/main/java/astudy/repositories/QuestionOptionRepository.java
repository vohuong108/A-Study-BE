package astudy.repositories;

import astudy.models.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
    @Query(
            value = "SELECT ID FROM question_option o WHERE o.question_id = :questionId AND o.is_correct = TRUE",
            nativeQuery = true)
    Long getAnswerByQuestionId(@Param("questionId") Long questionId);
}
