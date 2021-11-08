package astudy.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "course_student")
public class courseStudent {
    @EmbeddedId
    CourseStudentKey id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "enrolled_at")
    private Date enrolledAt;

    @Column(name = "progress")
    private int progress;
}
