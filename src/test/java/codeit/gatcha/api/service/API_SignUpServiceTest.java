package codeit.gatcha.api.service;

import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.security.service.API_SignUpService;
import codeit.gatcha.domain.user.DTO.SignUpDTO;
import codeit.gatcha.domain.user.DTO.UserDTO;
import codeit.gatcha.domain.user.entity.Authority;
import codeit.gatcha.domain.user.entity.GatchaUser;
import codeit.gatcha.domain.user.repo.IAuthorityRepo;
import codeit.gatcha.domain.user.repo.IUserRepo;
import codeit.gatcha.domain.user.service.signUp.EmailConfirmationService;
import codeit.gatcha.domain.user.service.signUp.SignUpService;
import codeit.gatcha.domain.user.service.signUp.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@ExtendWith(MockitoExtension.class)
public class API_SignUpServiceTest {
    @Mock
    private IUserRepo IUserRepo;

    @Mock
    private IAuthorityRepo authorityRepo;

    @Mock
    EmailConfirmationService emailConfirmationService;

    @Mock
    private UserService userService;

    API_SignUpService api_signUpService;

    @BeforeEach
    void setUp(){
        SignUpService signUpService = new SignUpService(IUserRepo, authorityRepo, null);
        api_signUpService = new API_SignUpService(signUpService, emailConfirmationService, userService, null);
    }

    @Test
    void givenAnEmailAlreadyUsed_GetConflictResponse(){
        API_SignUpService api_signUpService = new API_SignUpService(null, null, userService, null);

        doReturn(true).when(userService).emailIsUsed("test@email");
        SignUpDTO signUpDTO = new SignUpDTO("test@email", "pass");
        ResponseEntity<APIResponse> response = api_signUpService.signUpAndSendConfirmationEmail(signUpDTO);
        assertEquals(CONFLICT, response.getStatusCode());

        APIResponse body = response.getBody();
        assertEquals("The email test@email is already in use", body.getMessage());
        assertEquals(CONFLICT.value(), body.getStatusCode());
    }

    @Test
    void givenAValidSignUpDTO_SuccessfullyAddNewUser(){
        doReturn(false).when(userService).emailIsUsed("user@test");

        doReturn(Optional.of(new Authority())).when(authorityRepo).findByRole("ROLE_USER");

        SignUpDTO signUpDTO = new SignUpDTO("user@test", "pass");
        doReturn(GatchaUser.builder().email("user@test").build()).
                when(IUserRepo).
                save(Mockito.any());

        doNothing().when(emailConfirmationService).createAndSendConfirmationTokenToUser(Mockito.any());

        ResponseEntity<APIResponse> result = api_signUpService.signUpAndSendConfirmationEmail(signUpDTO);

        assertEquals(CREATED, result.getStatusCode());
        UserDTO user = (UserDTO) result.getBody().getBody();
        assertEquals("user@test", user.getEmail());
        assertEquals("A confirmation email has been sent. Check your inbox, please!", result.getBody().getMessage());
    }
}
