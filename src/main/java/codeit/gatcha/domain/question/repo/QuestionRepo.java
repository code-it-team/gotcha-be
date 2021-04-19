package codeit.gatcha.domain.question.repo;

import codeit.gatcha.domain.question.entity.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepo extends CrudRepository<Integer, Question> {
}
