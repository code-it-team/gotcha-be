package codeit.gatcha.user.entity;

import codeit.gatcha.security.entity.Authority;
import lombok.Data;

import javax.persistence.*;

@Data
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
}
