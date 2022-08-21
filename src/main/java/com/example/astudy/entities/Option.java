package com.example.astudy.entities;

import lombok.*;

import javax.persistence.*;

@Entity(name = "option")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Option {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long ID;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false, columnDefinition = "BOOLEAN")
    private Boolean isCorrect;

    @Column(nullable = false)
    private int optionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
