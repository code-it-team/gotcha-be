package codeit.gatcha.domain.question.service;

import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service @RequiredArgsConstructor
public class QuestionDeletionService {
    private final QuestionRepo questionRepo;

    public void invalidateQuestionByIdOrThrow(int id){
        Question question = questionRepo.
                findById(id).
                orElseThrow(EntityNotFoundException::new);
        question.setValid(false);
        questionRepo.save(question);
    }
}
