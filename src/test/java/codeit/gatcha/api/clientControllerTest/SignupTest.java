package codeit.gatcha.api.clientControllerTest;

import codeit.gatcha.api.controller.SignupController;
import codeit.gatcha.api.security.service.API_SignUpService;
import codeit.gatcha.domain.user.DTO.SignUpDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SignupController.class)
@ContextConfiguration(classes = SignupController.class)
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
    public void givenASignUpDTO_WhenDtoIsValid_ThenCallSignupService() throws Exception {
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

}
