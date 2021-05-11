package codeit.gatcha.domain.question.repo;

import codeit.gatcha.domain.question.entity.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepo extends CrudRepository<Question, Integer> {
    List<Question> findQuestionsByValidTrue();
}
