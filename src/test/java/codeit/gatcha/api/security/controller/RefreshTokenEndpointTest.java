package codeit.gatcha.api.security.controller;


import codeit.gatcha.api.security.service.AuthenticationApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = AuthenticationController.class)
@ContextConfiguration(classes = AuthenticationController.class)
@WithMockUser
public class RefreshTokenEndpointTest
{
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationApiService authenticationApiService;

    @Test
    public void givenTheRefreshToken_WhenRefreshTokenIsCalled_ThenReturn200() throws Exception
    {
        mockMvc.perform(get("/refreshToken")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

}
