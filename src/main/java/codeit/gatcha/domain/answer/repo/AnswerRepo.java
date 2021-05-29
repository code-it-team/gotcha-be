package codeit.gatcha.domain.answer.repo;

import codeit.gatcha.domain.answer.entity.Answer;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.user.entity.GatchaUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AnswerRepo extends CrudRepository<Answer, Integer> {
    Optional<Answer> findByQuestionAndUser(Question question, GatchaUser gatchaUser);
}
