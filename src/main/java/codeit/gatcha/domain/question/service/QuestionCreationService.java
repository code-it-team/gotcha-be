package codeit.gatcha.domain.question.service;

import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.IQuestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class QuestionCreationService {
    private final IQuestionRepo questionRepo;

    public Question createQuestion(String body){
        Question question = new Question(body);
        return questionRepo.save(question);
    }
}
