package astudy.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class QuizQuestionKey implements Serializable {
    @Column(name = "quiz_id")
    private Long quizId;

    @Column(name = "question_id")
    private Long questionId;
}
