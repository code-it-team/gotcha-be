package codeit.gatcha.APITest.clientControllerTest;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.API.client.DTO.question.outputDTO.User_QuestionDTO;
import codeit.gatcha.API.client.DTO.question.outputDTO.User_QuestionsDTO;
import codeit.gatcha.API.client.controller.QuestionController;
import codeit.gatcha.API.client.service.question.API_QuestionFetchService;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {
    @Mock
    QuestionRepo questionRepo;

/*    @Test
    void given_1_QuestionWithAnswerInDB_GetDTO(){
        API_QuestionFetchService api_questionFetchService = new API_QuestionFetchService(questionRepo);
        QuestionController questionController_admin = new QuestionController(api_questionFetchService);

        Question q1 = new Question("q1");
        doReturn(Collections.singletonList(q1)).when(questionRepo).findQuestionsByValidTrue();

        ResponseEntity<APIResponse> result = questionController_admin.getAllValidQuestionsWithUserAnswers();
        User_QuestionsDTO questionsDTO = (User_QuestionsDTO) result.getBody().getBody();

        assertEquals(OK.value(), result.getStatusCodeValue());
        assertEquals(OK, result.getStatusCode());
        List<User_QuestionDTO> questions = questionsDTO.getQuestions();

        assertEquals(1, questions.size());

        assertEquals("q1", questions.get(0).getBody());
        assertEquals("a1", questions.get(0).getAnswer());

    }*/

}
