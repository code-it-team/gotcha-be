package codeit.gatcha.APITest;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.API.admin.controller.QuestionController_Admin;
import codeit.gatcha.API.client.service.question.API_QuestionDeletionService;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.question.service.QuestionDeletionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class QuestionDeletionTest {
    @Mock
    QuestionRepo questionRepo;

    @Test
    public void givenAnIdOfNonExistingQuestion_DetectCantBeDeleted(){
        QuestionDeletionService questionDeletionService = new QuestionDeletionService(questionRepo);
        API_QuestionDeletionService api_questionDeletionService = new API_QuestionDeletionService(questionDeletionService);
        QuestionController_Admin questionController_admin = new QuestionController_Admin(null, null, api_questionDeletionService);

        doReturn(Optional.empty()).when(questionRepo).findById(22);

        ResponseEntity<APIResponse> result = questionController_admin.invalidateQuestionByIdOrThrow(22);

        assertEquals(NOT_FOUND, result.getStatusCode());
        assertEquals(NOT_FOUND.value(), result.getBody().getStatusCode());
        assertEquals("Question Not Found", result.getBody().getMessage());
    }

    @Test
    public void givenAnIdOfQuestion_DeleteIt(){
        QuestionDeletionService questionDeletionService = new QuestionDeletionService(questionRepo);
        API_QuestionDeletionService api_questionDeletionService = new API_QuestionDeletionService(questionDeletionService);
        QuestionController_Admin questionController_admin = new QuestionController_Admin(null, null, api_questionDeletionService);

        Question q = new Question();
        doReturn(Optional.of(q)).when(questionRepo).findById(23);
        doReturn(q).when(questionRepo).save(q);

        ResponseEntity<APIResponse> result = questionController_admin.invalidateQuestionByIdOrThrow(23);

        assertEquals(OK, result.getStatusCode());
        assertEquals(OK.value(), result.getBody().getStatusCode());
        assertFalse(q.isValid());
        assertEquals("Question Deleted", result.getBody().getMessage());
    }

}
