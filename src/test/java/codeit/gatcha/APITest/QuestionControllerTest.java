package codeit.gatcha.APITest;

import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.API.DTO.NewQuestionWithAnswers_DTO;
import codeit.gatcha.API.admin.question.controller.QuestionController_Admin;
import codeit.gatcha.domain.question.service.QuestionCreationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {
    @Mock
    QuestionCreationService questionCreationService;

    @Test
    void givenQuestionIsCreatedSuccessfully_CheckResponseMessage(){
        QuestionController_Admin questionController_admin = new QuestionController_Admin(questionCreationService);

        NewQuestionWithAnswers_DTO question = new NewQuestionWithAnswers_DTO();
        doReturn(null).when(questionCreationService).createQuestionWithAnswers(any(), any());
        ResponseEntity<APIResponse> response = questionController_admin.createNewQuestion(question);

        assertEquals(OK, response.getStatusCode());
        assertEquals(OK.value(), response.getBody().getStatusCode());
        assertEquals("The Question has been created successfully", response.getBody().getMessage());
    }


}
