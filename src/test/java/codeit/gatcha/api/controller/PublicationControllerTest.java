package codeit.gatcha.api.controller;

import codeit.gatcha.api.controller.PublicationController;
import codeit.gatcha.api.service.publication.API_PublicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PublicationController.class)
@ContextConfiguration(classes = PublicationController.class)
@WithMockUser
public class PublicationControllerTest {
    @MockBean
    API_PublicationService api_publicationService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void givenPublishAnswersEndpoint_WhenCalled_ThenReturn200() throws Exception
    {
        mockMvc.perform(post("/answers/publish")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAnswersLinkEndpointIsCalled_WhenCalled_ThenReturn200() throws Exception
    {
       mockMvc.perform(get("/answers/link"))
               .andExpect(status().isOk());
    }

    @Test
    public void givenPublishedAnswersByLinkEndpoint_WhenCalled_ThenReturn200() throws Exception
    {
        mockMvc.perform(get("/answers/published/test-link"))
                .andExpect(status().isOk());

        verify(api_publicationService).getPublishedAnswersByLink("test-link");
    }
}
