package astudy.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "course_student")
@Data
public class CourseStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "enrolled_at")
    private Date enrolledAt;

    @Column(name = "progress")
    private int progress;
}
