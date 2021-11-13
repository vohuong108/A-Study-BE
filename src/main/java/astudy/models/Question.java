package astudy.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question")
@Data
public class Question {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long ID;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(nullable = false)
    private int score;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    Quiz quiz;

    @OneToMany(mappedBy = "question")
    List<QuestionOption> options;

    @OneToMany(mappedBy = "question")
    List<Choosed> choosedList;
}
