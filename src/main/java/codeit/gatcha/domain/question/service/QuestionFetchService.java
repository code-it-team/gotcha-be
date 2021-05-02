package codeit.gatcha.domain.question.service;

import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service @RequiredArgsConstructor
public class QuestionFetchService {
    private final QuestionRepo questionRepo;

    public Question getQuestionByIdOrThrow(int questionId) {
        return questionRepo.
                findById(questionId).
                orElseThrow(() -> new EntityNotFoundException("No such question"));
    }
}
