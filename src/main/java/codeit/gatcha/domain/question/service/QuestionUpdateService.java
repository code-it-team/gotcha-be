package codeit.gatcha.domain.question.service;

import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.IQuestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service @RequiredArgsConstructor
public class QuestionUpdateService {
    private final IQuestionRepo questionRepo;

    public void updateQuestion(Integer id, String newBody) {
        Question question = questionRepo.
                findById(id).
                orElseThrow(EntityNotFoundException::new);

        question.setBody(newBody);
        questionRepo.save(question);
    }
}
