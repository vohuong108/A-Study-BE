package astudy.models;

import astudy.enums.Degree;
import astudy.enums.LectureStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "quiz")
public class Quiz {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long ID;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(nullable = false)
    private int maxScore;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Degree degree;

    @Column(nullable = false)
    private int time;

    @Column(nullable = false)
    private Date releaseDate;

    @Column(nullable = false)
    private Date closeDate;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private LectureStatus quizStatus;

    @Column(nullable = false)
    private Boolean state;

    @Column(nullable = false)
    private int attemptAllow;

    @ManyToOne
    @JoinColumn(name = "week_id", nullable = false)
    private Week week;

    @OneToMany(mappedBy = "quiz")
    List<Submit> submits;

    @OneToMany(mappedBy = "quiz")
    List<QuizQuestion> questions;
}
