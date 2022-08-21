package com.example.astudy.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long ID;

    @Column(length = 10)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Lob
    @Column(length = Integer.MAX_VALUE)
    private byte[] avatar;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public void update(UserProfile obj) {
        this.firstName = obj.firstName;
        this.lastName = obj.lastName;
        this.phone = obj.phone;
        this.address = obj.address;
        this.avatar = obj.avatar;
    }
}
