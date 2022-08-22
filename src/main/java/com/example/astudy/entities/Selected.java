package com.example.astudy.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "selected")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Selected {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "submit_id", nullable = false)
    private Submit submit;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

}
