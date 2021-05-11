package codeit.gatcha.domain.question.service;

import codeit.gatcha.domain.answer.entity.Answer;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class QuestionCreationService {
    private final QuestionRepo questionRepo;

    public Question createQuestionWithAnswers(String body, List<String> answersAsStrings){
        Set<Answer> answers = createAnswersFromStrings(answersAsStrings);
        Question question = new Question(body);
        addAnswersToQuestion(question, answers);
        return questionRepo.save(question);
    }

    private Set<Answer> createAnswersFromStrings(List<String> answers){
        return answers.
                stream().
                map(Answer::new).
                collect(Collectors.toSet());
    }

    private void addAnswersToQuestion(Question question, Set<Answer> answers) {
        answers.forEach(a -> a.setQuestion(question));
        question.setAnswers(answers);
    }
}
