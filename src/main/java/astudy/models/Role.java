package astudy.models;

import astudy.enums.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private Long ID;

    public Role(Permission permission) {
        this.name = permission;
    }

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private Permission name;

    @OneToMany(mappedBy = "role")
    private Set<User> users;
}
