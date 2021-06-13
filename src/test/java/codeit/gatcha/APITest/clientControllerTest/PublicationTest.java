package codeit.gatcha.APITest.clientControllerTest;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.API.client.DTO.Publication.PublicationLinkDTO;
import codeit.gatcha.API.client.controller.PublicationController;
import codeit.gatcha.API.client.service.publication.API_PublicationService;
import codeit.gatcha.application.user.service.UserSessionService;
import codeit.gatcha.domain.answer.service.AnswerFetchService;
import codeit.gatcha.domain.publication.entity.Publication;
import codeit.gatcha.domain.publication.repo.PublicationRepo;
import codeit.gatcha.domain.user.entity.GatchaUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.postgresql.hostchooser.HostRequirement.any;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class PublicationTest {
    @Mock
    AnswerFetchService answerFetchService;
    @Mock
    UserSessionService userSessionService;
    @Mock
    PublicationRepo publicationRepo;

    PublicationController publicationController;
    API_PublicationService api_publicationService;

    @BeforeEach
    public void setUp(){
        api_publicationService = new API_PublicationService(answerFetchService, publicationRepo, userSessionService);
        publicationController = new PublicationController(api_publicationService);
    }

    @Test
    public void givenThatUserHasntAnsweredQuestions_WhenPublishing_Return400(){
        GatchaUser user = new GatchaUser();
        doReturn(user).when(userSessionService).getCurrentLoggedInUser();
        doReturn(true).when(answerFetchService).currentUserHasntAnsweredAllQuestions(user);
        ResponseEntity<APIResponse> result =  publicationController.publishAnswers();

        assertEquals(BAD_REQUEST, result.getStatusCode());
        assertEquals(BAD_REQUEST.value() ,result.getBody().getStatusCode());
        assertEquals("Please answer all questions before publishing", result.getBody().getMessage());
    }

    @Test
    public void givenThatUserHasAnsweredAllQuestions_DetectAlreadyPublished(){
        GatchaUser user = new GatchaUser();
        doReturn(user).when(userSessionService).getCurrentLoggedInUser();

        doReturn(false).when(answerFetchService).currentUserHasntAnsweredAllQuestions(user);
        doReturn(Optional.of(new Publication())).when(publicationRepo).findByGatchaUserAndPublishedIsTrue(user);
        ResponseEntity<APIResponse> result =  publicationController.publishAnswers();

        assertEquals(BAD_REQUEST, result.getStatusCode());
        assertEquals(BAD_REQUEST.value() ,result.getBody().getStatusCode());
        assertEquals("User has already published the answers", result.getBody().getMessage());

    }

    @Test
    public void givenThatUserHasAnsweredAllQuestions_PublishSuccessfully(){
        GatchaUser user = new GatchaUser();
        doReturn(user).when(userSessionService).getCurrentLoggedInUser();

        doReturn(false).when(answerFetchService).currentUserHasntAnsweredAllQuestions(user);
        doReturn(Optional.empty()).when(publicationRepo).findByGatchaUserAndPublishedIsTrue(user);
        when(publicationRepo.save(any())).then(returnsFirstArg());
        ResponseEntity<APIResponse> result =  publicationController.publishAnswers();
        Publication publication = (Publication) result.getBody().getBody();

        assertEquals(OK, result.getStatusCode());
        assertEquals(OK.value() ,result.getBody().getStatusCode());
        assertEquals("The answers have been published", result.getBody().getMessage());
        assertEquals(user, publication.getGatchaUser());
        assertNotNull(publication.getLinkUniqueString());
    }

    @Test
    public void givenAnswersNotPublished_WhenTryingToGetPublicationLink_Return400(){
        doReturn(Optional.empty()).when(publicationRepo).findByGatchaUserAndPublishedIsTrue(any());

        ResponseEntity<APIResponse> result = publicationController.getPublicationLinkOfUser();
        assertEquals(BAD_REQUEST, result.getStatusCode());
        assertEquals(BAD_REQUEST.value(), result.getBody().getStatusCode());
        assertEquals("This user hasn't published answers yet", result.getBody().getMessage());
    }

    @Test
    public void givenAnswersArePublished_WhenTryingToGetPublicationLink_GetIt(){
        Publication publication = Publication.builder().publicationDate(new Date()).linkUniqueString("asd").build();
        doReturn(Optional.of(publication)).when(publicationRepo).findByGatchaUserAndPublishedIsTrue(any());

        ResponseEntity<APIResponse> result = publicationController.getPublicationLinkOfUser();
        PublicationLinkDTO publicationLinkDTO = (PublicationLinkDTO) result.getBody().getBody();
        assertEquals(OK, result.getStatusCode());
        assertEquals(OK.value(), result.getBody().getStatusCode());
        assertEquals("success", result.getBody().getMessage());
        assertEquals("asd", publicationLinkDTO.getLink());
        assertFalse(publicationLinkDTO.getPublicationDate().isEmpty());
    }

}
