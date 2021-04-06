package codeit.gatcha.security.entity;

import codeit.gatcha.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data @NoArgsConstructor
@Entity
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToMany
    private Set<User> users;

    @Column(unique = true)
    private String role;

    public Authority(Set<User> users, String role) {
        this.users = users;
        this.role = role;
    }
}
