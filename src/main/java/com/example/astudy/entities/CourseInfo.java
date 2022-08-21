package com.example.astudy.entities;

import com.example.astudy.enums.CourseInfoType;
import lombok.*;

import javax.persistence.*;

@Entity(name = "course_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CourseInfoType infoType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
}
