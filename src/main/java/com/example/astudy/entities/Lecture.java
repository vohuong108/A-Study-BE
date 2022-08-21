package com.example.astudy.entities;


import lombok.*;

import javax.persistence.*;

@Entity(name = "lecture")
@Getter
@Setter
@DiscriminatorValue("LECTURE")
@NoArgsConstructor
public class Lecture extends WeekContent{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contentPath;
}
