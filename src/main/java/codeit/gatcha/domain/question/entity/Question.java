package codeit.gatcha.domain.question.entity;

import codeit.gatcha.domain.answer.entity.Answer;
import codeit.gatcha.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity @Data @NoArgsConstructor @Builder @AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String body;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "question", fetch = FetchType.EAGER)
    private Set<Answer> answers = new HashSet<>();

    private boolean valid = true;

    public Question(Integer id, String body, Set<Answer> answers) {
        this.id = id;
        this.body = body;
        this.answers = answers;
    }

    public Question(String body) {
        this.body = body;
    }
}
