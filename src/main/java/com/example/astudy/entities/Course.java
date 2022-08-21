package com.example.astudy.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(nullable = false, name = "release_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date releaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // if foreign key association that doesn't reference a primary key
    // it will throw don't cast, so you must be implements Serializable in referenced class.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_name", referencedColumnName = "username", nullable = false)
    private AppUser author;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<CourseInfo> courseInfos;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @org.hibernate.annotations.OrderBy(clause = "week_order ASC")
    private Set<Week> weeks;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<StudentCourse> studentCourses;

    public Course(Long ID, String name, String description, Date releaseDate) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
    }
}
