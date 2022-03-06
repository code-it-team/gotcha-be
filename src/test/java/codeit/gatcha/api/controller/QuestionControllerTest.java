package codeit.gatcha.api.controller;

import codeit.gatcha.api.DTO.question.outputDTO.User_QuestionsDTO;
import codeit.gatcha.api.errorhandler.ValidationAdvice;
import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.DTO.question.inputDTO.QuestionAnswer_DTO;
import codeit.gatcha.api.DTO.question.inputDTO.QuestionAnswers_DTO;
import codeit.gatcha.api.service.answer.API_AnswerSubmissionService;
import codeit.gatcha.api.service.question.API_QuestionFetchService;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = QuestionController.class)
@ContextConfiguration(classes = {QuestionController.class, ValidationAdvice.class})
@WithMockUser
public class QuestionControllerTest {
    @MockBean
    API_QuestionFetchService api_questionFetchService;

    @MockBean
    API_AnswerSubmissionService api_answerSubmissionService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void givenAnswersToTwoQuestion_ThenReturn200() throws Exception {
        QuestionAnswers_DTO questionAnswers_dto = new QuestionAnswers_DTO(Arrays.asList(
                new QuestionAnswer_DTO(1, "a1"),
                new QuestionAnswer_DTO(2, "a2"))
        );

        mockMvc.perform(post("/questions/submitAnswer")
                        .content(objectMapper.writeValueAsString(questionAnswers_dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(api_answerSubmissionService).submitQuestionsAnswers(any());
    }

    @Test
    public void givenDTOWithNoQuestions_ReturnBadRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/questions/submitAnswer")
                        .content(objectMapper.writeValueAsString(new QuestionAnswers_DTO()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andReturn();

        APIResponse apiResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), APIResponse.class);

        assertThat(apiResponse.getMessage()).isEqualTo("Must at least have one answer question pair");
    }

    @Test
    public void givenTheEndpointForAllQuestions_WhenCalled_ThenReturnThem() throws Exception {
        when(api_questionFetchService.getAllValidQuestionsWithUserAnswers_DTO())
                .thenReturn(new User_QuestionsDTO());

        MvcResult mvcResult = mockMvc.perform(get("/questions"))
                .andExpect(status().isOk())
                .andReturn();

        APIResponse apiResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), APIResponse.class);

        User_QuestionsDTO returnedUserQuestionsDTO = objectMapper.convertValue(apiResponse.getBody(), User_QuestionsDTO.class);

        assertThat(returnedUserQuestionsDTO).isNotNull();
        assertThat(apiResponse.getMessage()).isEqualTo("success");
    }
}
