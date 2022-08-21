package com.example.astudy.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "week")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Week {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private int weekOrder;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "week", cascade = CascadeType.ALL)
    @org.hibernate.annotations.OrderBy(clause = "content_order ASC")
    Set<WeekContent> weekContents;
}
