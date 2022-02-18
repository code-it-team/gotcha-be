package codeit.gatcha.domain.user.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "GATCHA_USER")
public class GatchaUser {
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

    public GatchaUser(String email, String password, boolean enabled, Authority authority) {
        this.authority = authority;
        this.email = email;
        this.password = password;
        this.enabled = enabled;
    }
}
