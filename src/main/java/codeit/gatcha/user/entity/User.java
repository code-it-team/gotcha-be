package codeit.gatcha.user.entity;

import codeit.gatcha.security.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data @NoArgsConstructor @Builder @AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @ManyToOne
    private Authority authority;

    @Email
    @Column(unique = true)
    private String email;

    private String password;

    private boolean enabled = true;

    public User(String email, String password, boolean enabled, Authority authority) {
        this.authority = authority;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }
}
