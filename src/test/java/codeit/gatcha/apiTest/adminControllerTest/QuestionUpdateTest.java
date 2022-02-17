package codeit.gatcha.apiTest.adminControllerTest;

import codeit.gatcha.api.admin.controller.QuestionController_Admin;
import codeit.gatcha.api.client.DTO.APIResponse;
import codeit.gatcha.api.client.DTO.question.outputDTO.Admin_QuestionDTO;
import codeit.gatcha.api.client.service.question.API_QuestionUpdateService;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.question.service.QuestionUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class QuestionUpdateTest {
    @Mock
    QuestionRepo questionRepo;

    QuestionUpdateService questionUpdateService;
    API_QuestionUpdateService api_questionUpdateService;
    QuestionController_Admin questionController_admin;

    @BeforeEach
    public void setup(){
        questionUpdateService = new QuestionUpdateService(questionRepo);
        api_questionUpdateService = new API_QuestionUpdateService(questionUpdateService);
        questionController_admin = new QuestionController_Admin(null, null, null, api_questionUpdateService);
    }

    @Test
    public void givenAQuestionIdToUpdate_DetectQuestionNotFound(){
        Admin_QuestionDTO questionDTO = new Admin_QuestionDTO("newBody", 1);

        doReturn(Optional.empty()).when(questionRepo).findById(1);

        ResponseEntity<APIResponse> result = questionController_admin.updateQuestionById(questionDTO);
        assertEquals(NOT_FOUND, result.getStatusCode());
        assertEquals(NOT_FOUND.value(), result.getBody().getStatusCode());
        assertEquals("Question Not Found", result.getBody().getMessage());
    }

    @Test
    public void givenAQuestionIdToUpdate_UpdateQuestion(){
        Admin_QuestionDTO questionDTO = new Admin_QuestionDTO("newBody", 11);
        Question question = new Question();

        doReturn(Optional.of(question)).when(questionRepo).findById(11);

        ResponseEntity<APIResponse> result = questionController_admin.updateQuestionById(questionDTO);
        assertEquals(OK, result.getStatusCode());
        assertEquals(OK.value(), result.getBody().getStatusCode());
        assertEquals("Question has been successfully updated", result.getBody().getMessage());
        assertEquals("newBody", question.getBody());
    }

}
