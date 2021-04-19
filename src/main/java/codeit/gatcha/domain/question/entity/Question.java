package codeit.gatcha.domain.question.entity;

import codeit.gatcha.domain.answer.Answer;
import codeit.gatcha.domain.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity @Data @NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false)
    private String body;

    @OneToMany
    private Set<Answer> answers = new HashSet<>();

    @ManyToMany
    private Set<User> users = new HashSet<>();

}
