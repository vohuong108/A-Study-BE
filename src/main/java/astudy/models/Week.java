package astudy.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "week")
public class Week {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long ID;

    @Column
    private int serialWeek;

    @Column(columnDefinition = "TEXT")
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "week")
    List<Quiz> quizs;

    @OneToMany(mappedBy = "week")
    List<Quiz> lectures;
}
