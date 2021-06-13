package codeit.gatcha.domain.publication.entity;

import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity @NoArgsConstructor @Getter @AllArgsConstructor @Builder
@Table(name = "GATCHA_PUBLICATION")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToOne
    @JoinColumn(unique = true)
    private GatchaUser gatchaUser;

    private Date publicationDate;
    private boolean published;
    private String linkUniqueString;


    public Publication(GatchaUser gatchaUser, Date publicationDate, boolean published) {
        this.gatchaUser = gatchaUser;
        this.publicationDate = publicationDate;
        this.published = published;
        this.linkUniqueString = UUID.randomUUID().toString().replace("-", "");
    }
}
