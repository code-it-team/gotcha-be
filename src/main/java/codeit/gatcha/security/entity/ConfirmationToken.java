package codeit.gatcha.security.entity;

import codeit.gatcha.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity @Data @NoArgsConstructor
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
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }
}
