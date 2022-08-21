package com.example.astudy.entities;

import com.example.astudy.enums.Degree;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity(name = "quiz")
@Getter
@Setter
@DiscriminatorValue("QUIZ")
@AllArgsConstructor
@NoArgsConstructor
public class Quiz extends WeekContent{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false, name = "max_score")
    private int maxScore;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Degree degree;

    @Column(nullable = false)
    private int time;

    @Column(nullable = false, name="close_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closeDate;

    @Column(nullable = false, name = "attempt_allow")
    private int attemptAllow;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    @org.hibernate.annotations.OrderBy(clause = "question_order ASC")
    private Set<Question> questions;

}
