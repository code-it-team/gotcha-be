package codeit.gatcha.api.security.controller;

import codeit.gatcha.api.security.dto.AuthenticationRequest;
import codeit.gatcha.api.security.dto.SignOutRequestDto;
import codeit.gatcha.api.security.service.AuthenticationApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class)
@ContextConfiguration(classes = AuthenticationController.class)
@WithMockUser
public class AuthenticationEndpointTest
{
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthenticationApiService authenticationApiService;

    @Test
    public void givenAnAuthenticationRequest_WhenAuthenticationEndpointIsCalled_Return200() throws Exception
    {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("user", "pass");

         mockMvc
                .perform(post("/authenticate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest))
                )
                .andExpect(status().isOk());

        verify(authenticationApiService).verifyAndCreateAuthToken(any(AuthenticationRequest.class));
    }

    @Test
    public void givenASignOutRequest_Return200() throws Exception
    {
        SignOutRequestDto signOutRequestDto = new SignOutRequestDto("refreshToken");

        mockMvc.perform(post("/signout")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signOutRequestDto))
        )
        .andExpect(status().isOk());

        verify(authenticationApiService).signout(any(SignOutRequestDto.class));
    }
}
