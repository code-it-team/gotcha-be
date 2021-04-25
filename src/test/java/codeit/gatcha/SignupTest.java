package codeit.gatcha;

import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.API.service.API_SignUpService;
import codeit.gatcha.domain.user.DTO.SignUpDTO;
import codeit.gatcha.domain.user.DTO.UserDTO;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import codeit.gatcha.domain.user.service.signUp.EmailConfirmationService;
import codeit.gatcha.domain.user.service.signUp.SignUpService;
import codeit.gatcha.application.security.entity.Authority;
import codeit.gatcha.application.security.repo.AuthorityRepo;
import codeit.gatcha.domain.user.service.signUp.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@ExtendWith(MockitoExtension.class)
public class SignupTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private AuthorityRepo authorityRepo;
    @Mock
    EmailConfirmationService emailConfirmationService;
    @InjectMocks
    private API_SignUpService api_signUpService;
    @Mock
    private UserService userService;

    @Test
    void givenAnEmailAlreadyUsed_GetConflictResponse(){
        doReturn(true).when(userService).emailIsUsed("test@email");
        SignUpDTO signUpDTO = new SignUpDTO("test@email", "pass");
        ResponseEntity<APIResponse> response = api_signUpService.signUpAndSendConfirmationEmail(signUpDTO);
        assertEquals(CONFLICT, response.getStatusCode());

        APIResponse body = response.getBody();
        assertEquals("The email test@email is already in use", body.getResponse());
        assertEquals(CONFLICT.value(), body.getStatusCode());
    }

    @Test
    void givenAValidSignUpDTO_SuccessfullyAddNewUser(){

        doReturn(false).when(userService).emailIsUsed("user@test");

        doReturn(new Authority()).when(authorityRepo).findByRole("ROLE_USER");

        SignUpDTO signUpDTO = new SignUpDTO("user@test", "pass");
        doReturn(User.builder().email("user@test")).
                when(userRepo).
                save(Mockito.any());

        doNothing().when(emailConfirmationService).createAndSendConfirmationTokenToUser(Mockito.any());

        ResponseEntity<APIResponse> result = api_signUpService.signUpAndSendConfirmationEmail(signUpDTO);

        assertEquals(CREATED, result.getStatusCode());
        UserDTO user = (UserDTO) result.getBody().getResponse();
        assertEquals("user@test", user.getEmail());
    }


}
