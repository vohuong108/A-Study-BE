package astudy.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long ID;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role")
    private Role role;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Course> courses;

    @OneToOne
    @JoinColumn(name = "profile_id")
    private UserProfile userProfile;

    @OneToMany(mappedBy = "student")
    List<CourseStudent> courseStudents;

//    @OneToMany(mappedBy = "student")
//    List<Submit> submits;
}
