package codeit.gatcha.security.entity;

import codeit.gatcha.user.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToMany
    private Set<User> users;

    @Column(unique = true)
    private String role;
}
