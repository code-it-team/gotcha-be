package codeit.gatcha.domain;

import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.IQuestionRepo;
import codeit.gatcha.domain.question.service.QuestionCreationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
public class QuestionCreationTest {
    @Mock
    IQuestionRepo questionRepo;

    @Test
    void givenAnswers_CreateQuestion(){
        QuestionCreationService questionCreationService = new QuestionCreationService(questionRepo);

        doAnswer(returnsFirstArg()).when(questionRepo).save(any());
        Question question = questionCreationService.createQuestion("test question");

        assertEquals("test question", question.getBody());
    }


}
