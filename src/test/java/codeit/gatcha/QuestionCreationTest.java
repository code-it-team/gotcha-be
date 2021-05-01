package codeit.gatcha;

import codeit.gatcha.domain.answer.entity.Answer;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.question.service.QuestionCreationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
public class QuestionCreationTest {
    @Mock
    QuestionRepo questionRepo;

    @Test
    void givenAnswers_CreateQuestion(){
        QuestionCreationService questionCreationService = new QuestionCreationService(questionRepo);
        Set<Answer> answers = new HashSet<>(Arrays.asList(
                new Answer("answer1"), new Answer("answer2")));

        doAnswer(returnsFirstArg()).when(questionRepo).save(any());
        Question question = questionCreationService.createQuestionWithAnswers("test question", answers);

        assertEquals("test question", question.getBody());
        assertEquals(2, question.getAnswers().size());

        Iterator<Answer> iterator = question.getAnswers().iterator();
        Answer answer1 = iterator.next();
        assertEquals("answer1", answer1.getBody());
        assertEquals(question, answer1.getQuestion());

        Answer answer2 = iterator.next();
        assertEquals("answer2", answer2.getBody());
        assertEquals(question, answer2.getQuestion());
    }


}
