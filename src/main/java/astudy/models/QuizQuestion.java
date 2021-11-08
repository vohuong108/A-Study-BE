package astudy.models;

import javax.persistence.*;

@Entity
@Table(name = "quiz_question")
public class QuizQuestion {
    @EmbeddedId
    QuizQuestionKey id;

    @ManyToOne
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;
}
