package codeit.gatcha.user.entity;

import codeit.gatcha.security.entity.Authority;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data @NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    private Authority authority;

    @Column(unique = true)
    private String userName;

    private String password;

    private boolean enabled = true;

    public User(String userName, String password, boolean enabled, Authority authority) {
        this.authority = authority;
        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
    }
}
