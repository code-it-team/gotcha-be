package codeit.gatcha.APITest.clientControllerTest;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.API.client.DTO.question.inputDTO.QuestionAnswer_DTO;
import codeit.gatcha.API.client.DTO.question.inputDTO.QuestionAnswers_DTO;
import codeit.gatcha.API.client.controller.QuestionController;
import codeit.gatcha.API.client.service.answer.API_AnswerSubmissionService;
import codeit.gatcha.application.user.service.UserSessionService;
import codeit.gatcha.domain.answer.entity.Answer;
import codeit.gatcha.domain.answer.repo.AnswerRepo;
import codeit.gatcha.domain.answer.service.AnswerSubmissionService;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.user.entity.GatchaUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class AnswerSubmissionTest {
    @Mock
    QuestionRepo questionRepo;
    @Mock
    UserSessionService userSessionService;
    @Mock
    AnswerRepo answerRepo;

    AnswerSubmissionService answerSubmissionService;
    API_AnswerSubmissionService api_answerSubmissionService;
    QuestionController questionController;

    @BeforeEach
    public void setup(){
        answerSubmissionService = new AnswerSubmissionService(questionRepo, userSessionService, answerRepo);
        api_answerSubmissionService = new API_AnswerSubmissionService(answerSubmissionService);
        questionController = new QuestionController(null, api_answerSubmissionService);
    }

    @Test
    public void givenAnswersToTwoQuestion_DetectQuestionNotFound(){
        doReturn(Optional.empty()).when(questionRepo).findById(1);

        QuestionAnswers_DTO questionAnswers_dto = new QuestionAnswers_DTO(Arrays.asList(
                new QuestionAnswer_DTO(1, "a1"),
                new QuestionAnswer_DTO(2, "a2"))
        );

        ResponseEntity<APIResponse> result = questionController.submitAnswers(questionAnswers_dto);
        APIResponse apiResponse = result.getBody();

        assertEquals(BAD_REQUEST, result.getStatusCode());
        assertEquals(BAD_REQUEST.value(), apiResponse.getStatusCode());
        assertNull(apiResponse.getBody());

        assertEquals("Question 1 wasn't found", apiResponse.getMessage());
    }

    @Test
    public void givenAnswerToQuestion_DetectAlreadyExistsAndUpdateIt(){
        Question question = new Question();
        Answer answer = Answer.builder().body("old body").build();
        GatchaUser gatchaUser = new GatchaUser();

        doReturn(gatchaUser).when(userSessionService).getCurrentLoggedInUser();
        doReturn(Optional.of(question)).when(questionRepo).findById(10);
        doReturn(Optional.of(answer)).when(answerRepo).findByQuestionAndUser(question, gatchaUser);
        doReturn(answer).when(answerRepo).save(answer);

        QuestionAnswers_DTO questionAnswers_dto = new QuestionAnswers_DTO(Collections.singletonList(new QuestionAnswer_DTO(10, "a1")));

        ResponseEntity<APIResponse> result = questionController.submitAnswers(questionAnswers_dto);

        assertEquals("a1", answer.getBody());
        assertEquals(OK, result.getStatusCode());
        assertEquals(OK.value(), result.getBody().getStatusCode());
        assertEquals("success", result.getBody().getMessage());
    }

    @Test
    public void givenAnswerToQuestion_CreateIt(){
        Question question = new Question();
        GatchaUser gatchaUser = new GatchaUser();

        doReturn(gatchaUser).when(userSessionService).getCurrentLoggedInUser();
        doReturn(Optional.of(question)).when(questionRepo).findById(100);
        doReturn(Optional.empty()).when(answerRepo).findByQuestionAndUser(question, gatchaUser);

        QuestionAnswers_DTO questionAnswers_dto = new QuestionAnswers_DTO(Collections.singletonList(new QuestionAnswer_DTO(100, "a1")));

        ResponseEntity<APIResponse> result = questionController.submitAnswers(questionAnswers_dto);

        assertEquals(OK, result.getStatusCode());
        assertEquals(OK.value(), result.getBody().getStatusCode());
        assertEquals("success", result.getBody().getMessage());
    }

}
