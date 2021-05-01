package codeit.gatcha.domain.user.entity;

import codeit.gatcha.application.security.entity.Authority;
import codeit.gatcha.domain.question.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany
    private Set<Question> questions = new HashSet<>();

    public User(String email, String password, boolean enabled, Authority authority) {
        this.authority = authority;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }
}
