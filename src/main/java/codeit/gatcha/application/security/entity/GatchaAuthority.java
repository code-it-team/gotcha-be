package codeit.gatcha.application.security.entity;

import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data @NoArgsConstructor
@Entity @Table(name = "GATCHA_AUTHORITY")
public class GatchaAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToMany
    private Set<GatchaUser> users;

    @Column(unique = true)
    private String role;

    public GatchaAuthority(Set<GatchaUser> users, String role) {
        this.users = users;
        this.role = role;
    }
}
