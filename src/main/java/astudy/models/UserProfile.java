package astudy.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
public class UserProfile {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
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

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private User user;

}
