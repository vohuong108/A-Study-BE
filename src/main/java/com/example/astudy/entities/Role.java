package com.example.astudy.entities;

import com.example.astudy.enums.AppUserRole;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long ID;

    @Column
    @Enumerated(EnumType.STRING)
    private AppUserRole name;
}
