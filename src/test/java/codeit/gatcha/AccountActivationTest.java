package codeit.gatcha;

import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import codeit.gatcha.domain.user.service.signUp.SignUpService;
import codeit.gatcha.application.security.entity.ConfirmationToken;
import codeit.gatcha.application.security.repo.ConfirmationTokenRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class AccountActivationTest {
    @Mock
    UserRepo userRepo;
    @Mock
    ConfirmationTokenRepo confirmationTokenRepo;
    @InjectMocks
    SignUpService signUpService;

/*    @Test
    void givenAConfirmationToken_DetectItsNotFound(){
        doReturn(Optional.empty()).when(confirmationTokenRepo).findByConfirmationToken("testToken");
        ResponseEntity<APIResponse> result = signUpService.confirmUserAccount("testToken");
        assertEquals(NOT_FOUND, result.getStatusCode());
        assertEquals("The token testToken isn't found", result.getBody().getResponse());
        assertEquals(NOT_FOUND.value(), result.getBody().getStatusCode());
    }*/

/*    @Test
    void givenAConfirmationToken_activateUser(){
        User user = User.builder().email("email.email").build();
        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        doReturn(Optional.of(confirmationToken)).when(confirmationTokenRepo).findByConfirmationToken("tokenTest");
        doReturn(null).when(userRepo).save(user);

        ResponseEntity<APIResponse> result = signUpService.confirmUserAccount("tokenTest");
        assertEquals(OK, result.getStatusCode());
        assertEquals("email.email account has been activated", result.getBody().getResponse());
        assertEquals(OK.value(), result.getBody().getStatusCode());
        assertTrue(user.isEnabled());
    }*/

}
