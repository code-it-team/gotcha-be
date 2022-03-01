package codeit.gatcha.api.clientControllerTest;

import codeit.gatcha.api.controller.SignupController;
import codeit.gatcha.api.errorHandler.ValidationAdvice;
import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.security.service.API_SignUpService;
import codeit.gatcha.domain.user.DTO.SignUpDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SignupController.class)
@ContextConfiguration(classes = {SignupController.class, ValidationAdvice.class})
@WithMockUser
public class SignupTest {
    @MockBean
    API_SignUpService api_signUpService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<SignUpDTO> signupCaptor;

    @Test
    public void givenASignUpDTO_WhenDtoIsValid_ThenReturn200() throws Exception {
        SignUpDTO signUpDTO = new SignUpDTO("email", "pass");

        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signUpDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(api_signUpService).signUpAndSendConfirmationEmail(signupCaptor.capture());

        SignUpDTO captured = signupCaptor.getValue();

        assertThat(captured.getEmail()).isEqualTo("email");
        assertThat(captured.getPassword()).isEqualTo("pass");
    }

    @Test
    public void givenASignupDto_WhenDtoIsInvalid_ThenDetectBadRequest() throws Exception {
        SignUpDTO signUpDTO = SignUpDTO.builder().email("email").build();

        MvcResult mvcResult = mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signUpDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andReturn();

        APIResponse apiResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                APIResponse.class
        );

        assertThat(apiResponse.getMessage()).isEqualTo("Password can't be blank");
    }

    @Test
    public void givenAConfirmationToken_ThenReturn200() throws Exception {
        mockMvc.perform(get("/confirm-account").param("token", "test"))
                .andExpect(status().isOk());

        verify(api_signUpService).confirmUserAccount("test");
    }

}
