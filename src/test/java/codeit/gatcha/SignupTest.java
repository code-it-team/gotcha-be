package codeit.gatcha;

import codeit.gatcha.application.global.DTO.SingleMessageResponse;
import codeit.gatcha.domain.user.DTO.SignUpDTO;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import codeit.gatcha.domain.user.service.signUp.EmailConfirmationService;
import codeit.gatcha.domain.user.service.signUp.SignUpService;
import codeit.gatcha.application.security.entity.Authority;
import codeit.gatcha.application.security.repo.AuthorityRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class SignupTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private AuthorityRepo authorityRepo;
    @Mock
    EmailConfirmationService emailConfirmationService;
    @InjectMocks
    private SignUpService signUpService;

    @Test
    void givenAnEmailAlreadyUsed_GetConflictResponse(){
        User user = User.builder().email("test@email").build();
        doReturn(Optional.of(user)).when(userRepo).findByEmail("test@email");
        SignUpDTO signUpDTO = new SignUpDTO("test@email", "pass");
        ResponseEntity<Object> response = signUpService.signUpAndSendConfirmationEmail(signUpDTO);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("The email test@email is already in use", ((SingleMessageResponse) response.getBody()).getResponse());
    }

    @Test
    void givenAValidSignUpDTO_SuccessfullyAddNewUser(){
        Authority authority = new Authority();
        doReturn(authority).when(authorityRepo).findByRole("ROLE_USER");

        SignUpDTO signUpDTO = new SignUpDTO("user@test", "pass");
        doReturn(null).when(userRepo).save(Mockito.any());

        doNothing().when(emailConfirmationService).createAndSendConfirmationTokenToUser(Mockito.any());

        ResponseEntity<Object> result = signUpService.signUpAndSendConfirmationEmail(signUpDTO);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        User user = (User) result.getBody();
        assertEquals("user@test", user.getEmail());
        assertEquals("pass", user.getPassword());
        assertFalse(user.isEnabled());
        assertEquals(authority, user.getAuthority());
    }


}
