package codeit.gatcha.domain.answer.repo;

import codeit.gatcha.domain.answer.entity.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepo extends CrudRepository<Answer, Integer> {
}
