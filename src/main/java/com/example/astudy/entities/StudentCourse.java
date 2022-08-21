package com.example.astudy.entities;

import com.example.astudy.entities.key.StudentCourseKey;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "student_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {
    @EmbeddedId
    private StudentCourseKey ID;

    // MapsId means that we tie those fields to a part of the key,
    // and they're the foreign keys of a many-to-one relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private AppUser student;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(nullable = false, name = "enrolled_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date enrolledAt;

    @Column(nullable = false)
    private int progress;

}
