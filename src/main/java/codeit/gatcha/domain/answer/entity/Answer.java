package codeit.gatcha.domain.answer.entity;

import codeit.gatcha.domain.question.entity.Question;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Data @NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Question question;
}
