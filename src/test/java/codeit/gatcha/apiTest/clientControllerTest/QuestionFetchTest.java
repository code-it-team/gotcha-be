package codeit.gatcha.apiTest.clientControllerTest;

import codeit.gatcha.api.client.DTO.APIResponse;
import codeit.gatcha.api.client.DTO.question.outputDTO.User_QuestionDTO;
import codeit.gatcha.api.client.DTO.question.outputDTO.User_QuestionsDTO;
import codeit.gatcha.api.client.controller.QuestionController;
import codeit.gatcha.api.client.service.question.API_QuestionFetchService;
import codeit.gatcha.common.user.service.UserSessionService;
import codeit.gatcha.domain.answer.entity.Answer;
import codeit.gatcha.domain.answer.repo.AnswerRepo;
import codeit.gatcha.domain.publication.entity.Publication;
import codeit.gatcha.domain.publication.repo.PublicationRepo;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class QuestionFetchTest {
    @Mock
    QuestionRepo questionRepo;
    @Mock
    AnswerRepo answerRepo;
    @Mock
    UserSessionService userSessionService;
    @Mock
    PublicationRepo publicationRepo;

    @Test
    void given_1_QuestionWithAnswerInDB_GetDTO(){
        API_QuestionFetchService api_questionFetchService = new API_QuestionFetchService(questionRepo, answerRepo, userSessionService, publicationRepo);
        QuestionController questionController = new QuestionController(api_questionFetchService, null);

        Question q1 = new Question("q1");
        doReturn(Collections.singletonList(q1)).when(questionRepo).findQuestionsByValidTrue();

        Answer answer = Answer.builder().body("a1").build();
        doReturn(Optional.of(answer)).when(answerRepo).findByQuestionAndUser(q1, null);

        doReturn(Optional.of(new Publication())).when(publicationRepo).findByGatchaUserAndPublishedIsTrue(any());

        ResponseEntity<APIResponse> result = questionController.getAllValidQuestionsWithUserAnswers();
        User_QuestionsDTO questionsDTO = (User_QuestionsDTO) result.getBody().getBody();

        assertEquals(OK.value(), result.getStatusCodeValue());
        assertEquals(OK, result.getStatusCode());
        List<User_QuestionDTO> questions = questionsDTO.getQuestions();
        assertEquals(1, questions.size());
        assertEquals("q1", questions.get(0).getBody());
        assertEquals("a1", questions.get(0).getAnswer());
        assertTrue(questionsDTO.isPublished());
    }

    @Test
    void given_1_QuestionWithNoAnswerInDB_GetDTO(){
        API_QuestionFetchService api_questionFetchService = new API_QuestionFetchService(questionRepo, answerRepo, userSessionService, publicationRepo);
        QuestionController questionController = new QuestionController(api_questionFetchService, null);

        Question q1 = new Question("q2");
        doReturn(Collections.singletonList(q1)).when(questionRepo).findQuestionsByValidTrue();

        doReturn(Optional.empty()).when(answerRepo).findByQuestionAndUser(q1, null);

        ResponseEntity<APIResponse> result = questionController.getAllValidQuestionsWithUserAnswers();
        User_QuestionsDTO questionsDTO = (User_QuestionsDTO) result.getBody().getBody();

        assertEquals(OK.value(), result.getStatusCodeValue());
        assertEquals(OK, result.getStatusCode());
        List<User_QuestionDTO> questions = questionsDTO.getQuestions();
        assertEquals(1, questions.size());
        assertEquals("q2", questions.get(0).getBody());
        assertEquals("", questions.get(0).getAnswer());
    }

}
