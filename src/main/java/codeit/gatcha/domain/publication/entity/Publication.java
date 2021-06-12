package codeit.gatcha.domain.publication.entity;

import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity @NoArgsConstructor @Getter @AllArgsConstructor
@Table(name = "GATCHA_PUBLICATION")
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    @OneToOne
    private GatchaUser gatchaUser;
    private Date publicationDate;
    private boolean published;


    public Publication(GatchaUser gatchaUser, Date publicationDate, boolean published) {
        this.gatchaUser = gatchaUser;
        this.publicationDate = publicationDate;
        this.published = published;
    }
}
