package astudy.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "week")
@Getter
@Setter
public class Week {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long ID;

    @Column(name = "serial_week", nullable = false)
    private int serialWeek;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "week")
    List<Quiz> quizs;

    @OneToMany(mappedBy = "week")
    List<Lecture> lectures;
}
