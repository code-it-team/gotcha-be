package codeit.gatcha.domain.question.entity;

import lombok.*;
import javax.persistence.*;

@Entity @Data @NoArgsConstructor @Builder @AllArgsConstructor
@Table(name = "GATCHA_QUESTION")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private boolean valid = true;

    public Question(Integer id, String body) {
        this.id = id;
        this.body = body;
    }

    public Question(String body) {
        this.body = body;
    }
}
