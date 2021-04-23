package codeit.gatcha;

import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import codeit.gatcha.domain.user.service.signUp.SignUpService;
import codeit.gatcha.security.entity.ConfirmationToken;
import codeit.gatcha.security.repo.ConfirmationTokenRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class AccountActivationTest {
    @Mock
    UserRepo userRepo;
    @Mock
    ConfirmationTokenRepo confirmationTokenRepo;
    @InjectMocks
    SignUpService signUpService;

    @Test
    void givenAConfirmationToken_DetectItsNotFound(){
        doReturn(Optional.empty()).when(confirmationTokenRepo).findByConfirmationToken("testToken");
        ResponseEntity<String> result = signUpService.confirmUserAccount("testToken");
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("The token testToken isn't found", result.getBody());
    }

    @Test
    void givenAConfirmationToken_activateUser(){
        User user = User.builder().email("email.email").build();
        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        doReturn(Optional.of(confirmationToken)).when(confirmationTokenRepo).findByConfirmationToken("tokenTest");
        doReturn(null).when(userRepo).save(user);

        ResponseEntity<String> result = signUpService.confirmUserAccount("tokenTest");
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("email.email account has been activated", result.getBody());
        assertTrue(user.isEnabled());
    }

}
