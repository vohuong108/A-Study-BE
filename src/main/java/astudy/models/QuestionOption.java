package astudy.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "question_option")
@Data
public class QuestionOption {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long ID;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "BOOLEAN", name = "is_correct")
    private boolean isCorrect;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

//    @OneToMany(mappedBy = "questionOption")
//    private List<Choosed> choosedList;
}
