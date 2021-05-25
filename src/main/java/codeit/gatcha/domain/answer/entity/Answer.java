package codeit.gatcha.domain.answer.entity;

import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @NoArgsConstructor @AllArgsConstructor @Getter @Table(name = "GATCHA_ANSWER")
public class Answer {
    @Id
    private Integer id;

    private String body;

    @ManyToOne
    Question question;

    @ManyToOne
    GatchaUser user;
}
