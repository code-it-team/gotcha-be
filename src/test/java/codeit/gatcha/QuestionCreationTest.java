package codeit.gatcha;

import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.question.service.QuestionCreationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
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

        doAnswer(returnsFirstArg()).when(questionRepo).save(any());
        Question question = questionCreationService.createQuestionWithAnswers("test question", "answer");

        assertEquals("test question", question.getBody());
        assertEquals("answer", question.getAnswer());
    }


}
