package astudy.models;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "choosed")
public class Choosed {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long ID;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "submit_id", nullable = false)
    private Submit submit;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private QuestionOption questionOption;

}
