package codeit.gatcha.api.adminControllerTest;

import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.DTO.question.inputDTO.NewQuestion_DTO;
import codeit.gatcha.api.DTO.question.outputDTO.Admin_QuestionDTO;
import codeit.gatcha.api.DTO.question.outputDTO.Admin_QuestionsDTO;
import codeit.gatcha.api.controller.QuestionController_Admin;
import codeit.gatcha.api.service.question.API_QuestionDeletionService;
import codeit.gatcha.api.service.question.API_QuestionFetchService;
import codeit.gatcha.api.service.question.API_QuestionUpdateService;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.service.QuestionCreationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = QuestionController_Admin.class)
@ContextConfiguration(classes = QuestionController_Admin.class)
@WithMockUser
public class QuestionControllerTest {

    @MockBean
    QuestionCreationService questionCreationService;
    @MockBean
    API_QuestionFetchService api_questionFetchService;
    @MockBean
    API_QuestionDeletionService api_questionDeletionService;
    @MockBean
    API_QuestionUpdateService api_questionUpdateService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void givenQuestionIsCreatedSuccessfully_CheckResponseMessage() throws Exception {
        NewQuestion_DTO question = new NewQuestion_DTO("test question");

        doReturn(null).when(questionCreationService).createQuestion(any());

        MvcResult mvcResult = mockMvc.perform(post("/admin/createQuestion")
                        .content(objectMapper.writeValueAsString(question))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        APIResponse apiResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                APIResponse.class);

        assertThat(apiResponse.getMessage()).isEqualTo("The Question has been created successfully");
    }

    @Test
    void given_2_QuestionsInDB_GetTheirDTOs() throws Exception {
        Admin_QuestionDTO admin_questionDTO1 = new Admin_QuestionDTO(new Question(1, "q1"));

        Admin_QuestionDTO admin_questionDTO2 = new Admin_QuestionDTO(new Question(2, "q2"));

        Admin_QuestionsDTO admin_questionsDTO = new Admin_QuestionsDTO(Arrays.asList(admin_questionDTO1, admin_questionDTO2));

        doReturn(admin_questionsDTO).when(api_questionFetchService).getAllValidQuestions_DTO();

        MvcResult mvcResult = mockMvc.perform(get("/admin/questions"))
                .andExpect(status().isOk())
                .andReturn();

        APIResponse apiResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                APIResponse.class);

        assertThat(apiResponse.getMessage()).isEqualTo("success");

        Admin_QuestionsDTO body = objectMapper.convertValue(
                apiResponse.getBody(),
                Admin_QuestionsDTO.class);

        List<Admin_QuestionDTO> questions = body.getQuestions();

        assertEquals(2, questions.size());

        assertEquals("q1", questions.get(0).getBody());
        assertEquals(1, questions.get(0).getId());

        assertEquals("q2", questions.get(1).getBody());
        assertEquals(2, questions.get(1).getId());
    }
}
