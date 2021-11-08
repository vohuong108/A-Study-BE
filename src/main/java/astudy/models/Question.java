package astudy.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long ID;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(nullable = false)
    private int score;

    @OneToMany(mappedBy = "question")
    List<QuizQuestion> quizs;

    @OneToMany(mappedBy = "question")
    List<QuestionOption> options;

    @OneToMany(mappedBy = "question")
    List<Choosed> choosedList;
}
