package codeit.gatcha.domain.answer.repo;

import codeit.gatcha.domain.answer.entity.Answer;
import org.springframework.data.repository.CrudRepository;

public interface AnswerRepo extends CrudRepository<Integer, Answer> {
}
