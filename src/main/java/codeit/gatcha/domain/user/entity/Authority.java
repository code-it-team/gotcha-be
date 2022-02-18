package codeit.gatcha.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AUTHORITY")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authority
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(unique = true)
    private String role;

    @OneToMany
    private Set<GatchaUser> users;

}
