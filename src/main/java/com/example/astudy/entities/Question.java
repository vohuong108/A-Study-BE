package com.example.astudy.entities;

import com.example.astudy.enums.QuestionType;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "question")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long ID;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private int questionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @org.hibernate.annotations.OrderBy(clause = "option_order ASC")
    private Set<Option> options;

}
