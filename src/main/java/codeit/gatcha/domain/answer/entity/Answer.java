package codeit.gatcha.domain.answer.entity;

import codeit.gatcha.api.DTO.question.inputDTO.QuestionAnswer_DTO;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.*;

import javax.persistence.*;

@Entity @NoArgsConstructor @AllArgsConstructor @Getter @Table(name = "GATCHA_ANSWER") @Builder @Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(nullable = false)
    Question question;

    @ManyToOne
    @JoinColumn(nullable = false)
    GatchaUser user;

    public Answer (QuestionAnswer_DTO questionAnswer_dto, Question question, GatchaUser user){
        this.body = questionAnswer_dto.getAnswer();
        this.question = question;
        this.user = user;
    }
}
