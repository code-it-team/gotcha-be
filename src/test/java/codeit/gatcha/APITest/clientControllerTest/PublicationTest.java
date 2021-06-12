package codeit.gatcha.APITest.clientControllerTest;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.API.client.controller.PublicationController;
import codeit.gatcha.API.client.service.publication.API_PublicationService;
import codeit.gatcha.application.user.service.UserSessionService;
import codeit.gatcha.domain.answer.service.AnswerFetchService;
import codeit.gatcha.domain.publication.entity.Publication;
import codeit.gatcha.domain.publication.repo.PublicationRepo;
import codeit.gatcha.domain.user.entity.GatchaUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class PublicationTest {
    @Mock
    AnswerFetchService answerFetchService;
    @Mock
    UserSessionService userSessionService;
    @Mock
    PublicationRepo publicationRepo;

    @Test
    public void givenThatUserHasntAnsweredQuestions_WhenPublishing_Return400(){
        API_PublicationService api_publicationService = new API_PublicationService(answerFetchService, null, userSessionService);
        PublicationController publicationController = new PublicationController(api_publicationService);

        GatchaUser user = new GatchaUser();
        doReturn(user).when(userSessionService).getCurrentLoggedInUser();
        doReturn(true).when(answerFetchService).currentUserHasntAnsweredAllQuestions(user);
        ResponseEntity<APIResponse> result =  publicationController.publishAnswers();

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value() ,result.getBody().getStatusCode());
        assertEquals("Please answer all questions before publishing", result.getBody().getMessage());
    }

    @Test
    public void givenThatUserHasAnsweredAllQuestions_DetectAlreadyPublished(){
        API_PublicationService api_publicationService = new API_PublicationService(answerFetchService, publicationRepo, userSessionService);
        PublicationController publicationController = new PublicationController(api_publicationService);

        GatchaUser user = new GatchaUser();
        doReturn(user).when(userSessionService).getCurrentLoggedInUser();

        doReturn(false).when(answerFetchService).currentUserHasntAnsweredAllQuestions(user);
        doReturn(Optional.of(new Publication())).when(publicationRepo).findByGatchaUserAndPublishedIsTrue(user);
        ResponseEntity<APIResponse> result =  publicationController.publishAnswers();

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value() ,result.getBody().getStatusCode());
        assertEquals("User has already published the answers", result.getBody().getMessage());

    }

    @Test
    public void givenThatUserHasAnsweredAllQuestions_PublishSuccessfully(){
        API_PublicationService api_publicationService = new API_PublicationService(answerFetchService, publicationRepo, userSessionService);
        PublicationController publicationController = new PublicationController(api_publicationService);

        GatchaUser user = new GatchaUser();
        doReturn(user).when(userSessionService).getCurrentLoggedInUser();

        doReturn(false).when(answerFetchService).currentUserHasntAnsweredAllQuestions(user);
        doReturn(Optional.empty()).when(publicationRepo).findByGatchaUserAndPublishedIsTrue(user);
        doReturn(null).when(publicationRepo).save(Mockito.any());
        ResponseEntity<APIResponse> result =  publicationController.publishAnswers();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(HttpStatus.OK.value() ,result.getBody().getStatusCode());
        assertEquals("The answers have been published", result.getBody().getMessage());

    }

}
