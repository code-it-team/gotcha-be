package codeit.gatcha.domain.question.service;

import codeit.gatcha.domain.answer.entity.Answer;
import codeit.gatcha.domain.question.entity.Question;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class QuestionCreationService {

    public Question createQuestionWithAnswers(String body, Set<Answer> answers){
        Question question = Question.builder().body(body).build();
        addAnswersToQuestion(question, answers);
        return question;
    }

    private void addAnswersToQuestion(Question question, Set<Answer> answers) {
        answers.forEach(a -> a.setQuestion(question));
        question.setAnswers(answers);
    }

}
