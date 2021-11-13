package astudy.models;

import astudy.enums.Degree;
import astudy.enums.LectureStatus;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "quiz")
@Data
public class Quiz {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long ID;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(nullable = false, name = "max_score")
    private int maxScore;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Degree degree;

    @Column(nullable = false)
    private int time;

    @Column(nullable = false, name = "release_date")
    private Date releaseDate;

    @Column(nullable = false, name="close_date")
    private Date closeDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private LectureStatus quizStatus;

    @Column(nullable = false, name = "attempt_allow")
    private int attemptAllow;

    @Column(nullable = false, name = "index_lecture")
    private int indexLecture;

    @ManyToOne
    @JoinColumn(name = "week_id", nullable = false)
    private Week week;

    @OneToMany(mappedBy = "quiz")
    List<Submit> submits;

    @OneToMany(mappedBy = "quiz")
    List<Question> questions;
}
