package codeit.gatcha.APITest.adminControllerTest;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.API.client.DTO.question.inputDTO.NewQuestionWithAnswers_DTO;
import codeit.gatcha.API.client.DTO.question.outputDTO.QuestionDTO;
import codeit.gatcha.API.client.DTO.question.outputDTO.QuestionsDTO;
import codeit.gatcha.API.admin.controller.QuestionController_Admin;
import codeit.gatcha.API.client.service.question.API_QuestionFetchService;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.question.service.QuestionCreationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {
    @Mock
    QuestionCreationService questionCreationService;
    @Mock
    QuestionRepo questionRepo;

    @Test
    void givenQuestionIsCreatedSuccessfully_CheckResponseMessage(){
        QuestionController_Admin questionController_admin = new QuestionController_Admin(questionCreationService, null, null);

        NewQuestionWithAnswers_DTO question = new NewQuestionWithAnswers_DTO();
        doReturn(null).when(questionCreationService).createQuestionWithAnswers(any(), any());
        ResponseEntity<APIResponse> response = questionController_admin.createNewQuestion(question);

        assertEquals(OK, response.getStatusCode());
        assertEquals(OK.value(), response.getBody().getStatusCode());
        assertEquals("The Question has been created successfully", response.getBody().getMessage());
    }

    @Test
    void given_2_QuestionsInDB_GetTheirDTOs(){
        API_QuestionFetchService api_questionFetchService = new API_QuestionFetchService(questionRepo);
        QuestionController_Admin questionController_admin = new QuestionController_Admin(null, api_questionFetchService, null);

        Question q1 = new Question("q1", "a1");
        Question q2 = new Question("q2", "a2");
        doReturn(Arrays.asList(q1, q2)).when(questionRepo).findQuestionsByValidTrue();

        ResponseEntity<APIResponse> result = questionController_admin.getAllValidQuestions();
        QuestionsDTO questionsDTO = (QuestionsDTO) result.getBody().getBody();

        assertEquals(OK.value(), result.getStatusCodeValue());
        assertEquals(OK, result.getStatusCode());
        List<QuestionDTO> questions = questionsDTO.getQuestions();

        assertEquals(2, questions.size());

        assertEquals("q1", questions.get(0).getBody());
        assertEquals("a1", questions.get(0).getAnswer());

        assertEquals("q2", questions.get(1).getBody());
        assertEquals("a2", questions.get(1).getAnswer());
    }
}
