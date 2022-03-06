package codeit.gatcha.api.service;

import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.service.publication.API_PublicationService;
import codeit.gatcha.common.user.service.UserSessionService;
import codeit.gatcha.domain.answer.repo.IAnswerRepo;
import codeit.gatcha.domain.answer.service.AnswerFetchService;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.IQuestionRepo;
import codeit.gatcha.domain.user.entity.GatchaUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class API_PublicationServiceTest {
    @Mock
    IQuestionRepo questionRepo;

    @Mock
    IAnswerRepo answerRepo;

    @Mock
    UserSessionService userSessionService;

    API_PublicationService api_publicationService;

    @BeforeEach
    public void setup()
    {
        AnswerFetchService answerFetchService = new AnswerFetchService(answerRepo, questionRepo);
        api_publicationService = new API_PublicationService(answerFetchService, null, userSessionService, null);
    }

    @Test
    public void givenUser_WhenHasntAnsweredAllQuestions_ReturnBadRequest()
    {
        GatchaUser gatchaUser = new GatchaUser();

        when(userSessionService.getCurrentLoggedInUser()).thenReturn(gatchaUser);

        Question question = new Question();

        when(questionRepo.findQuestionsByValidTrue()).thenReturn(List.of(question));

        when(answerRepo.findByQuestionAndUser(question, gatchaUser)).thenReturn(Optional.empty());

        ResponseEntity<APIResponse> result = api_publicationService.publishAnswers();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result.getBody().getMessage()).isEqualTo("Please answer all questions before publishing");
    }

}
