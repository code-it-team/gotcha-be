package codeit.gatcha.APITest.clientControllerTest;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.API.client.controller.PublicationController;
import codeit.gatcha.API.client.service.publication.API_PublicationService;
import codeit.gatcha.domain.answer.service.AnswerFetchService;
import codeit.gatcha.domain.publication.entity.Publication;
import codeit.gatcha.domain.publication.service.PublicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class PublicationTest {
    @Mock
    AnswerFetchService answerFetchService;

    @Test
    public void givenThatUserHasntAnsweredQuestions_WhenPublishing_Return400(){
        API_PublicationService api_publicationService = new API_PublicationService(null, answerFetchService);
        PublicationController publicationController = new PublicationController(api_publicationService);

        doReturn(true).when(answerFetchService).currentUserHasntAnsweredAllQuestions();
        ResponseEntity<APIResponse> result =  publicationController.publishAnswers();

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value() ,result.getBody().getStatusCode());
        assertEquals("Please answer all questions before publishing", result.getBody().getMessage());
    }



}
