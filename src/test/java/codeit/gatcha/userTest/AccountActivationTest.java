package codeit.gatcha.userTest;

import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.security.entity.ConfirmationToken;
import codeit.gatcha.api.security.service.API_SignUpService;
import codeit.gatcha.api.security.service.ConfirmationTokenService;
import codeit.gatcha.domain.user.entity.GatchaUser;
import codeit.gatcha.domain.user.repo.IUserRepo;
import codeit.gatcha.domain.user.service.signUp.SignUpService;
import codeit.gatcha.api.security.repo.ConfirmationTokenRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    IUserRepo IUserRepo;
    @Mock
    ConfirmationTokenRepo confirmationTokenRepo;
    @Mock
    ConfirmationTokenService confirmationTokenService;

    SignUpService signUpService;
    API_SignUpService api_signUpService;

    @BeforeEach
    void setUp(){
        signUpService = new SignUpService(IUserRepo, null, confirmationTokenRepo);
        api_signUpService = new API_SignUpService(signUpService, null, null, confirmationTokenService);
    }

    @Test
    void givenAConfirmationToken_DetectItsNotFound(){
        doReturn(true).when(confirmationTokenService).confirmationTokenDoesntExist("testToken");
        ResponseEntity<APIResponse> result = api_signUpService.confirmUserAccount("testToken");
        assertEquals(NOT_FOUND, result.getStatusCode());
        assertEquals("The token testToken isn't found", result.getBody().getMessage());
        assertEquals(NOT_FOUND.value(), result.getBody().getStatusCode());
    }

    @Test
    void givenAConfirmationToken_activateUser(){
        GatchaUser user = new GatchaUser();
        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        doReturn(false).when(confirmationTokenService).confirmationTokenDoesntExist("tokenTest");
        doReturn(null).when(IUserRepo).save(user);
        doReturn(Optional.of(confirmationToken)).when(confirmationTokenRepo).findByConfirmationToken("tokenTest");

        ResponseEntity<APIResponse> result = api_signUpService.confirmUserAccount("tokenTest");
        assertEquals(OK, result.getStatusCode());
        assertEquals("The account has been activated", result.getBody().getMessage());
        assertEquals(OK.value(), result.getBody().getStatusCode());
        assertTrue(user.isEnabled());
    }

}
