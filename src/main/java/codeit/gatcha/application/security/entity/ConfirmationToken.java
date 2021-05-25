package codeit.gatcha.application.security.entity;

import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity @Data @NoArgsConstructor @Table(name = "GATCHA_CONFIRMATION_TOKEN")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String confirmationToken;

    @Column(nullable = false)
    private Date createdDate;

    @OneToOne
    @JoinColumn(nullable = false)
    private GatchaUser user;

    public ConfirmationToken(GatchaUser user) {
        this.user = user;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }
}
