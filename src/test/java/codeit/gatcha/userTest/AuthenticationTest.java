package codeit.gatcha.userTest;

import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.API.controller.AuthenticationController;
import codeit.gatcha.application.security.DTO.AuthenticationRequest;
import codeit.gatcha.application.security.DTO.AuthenticationResponse;
import codeit.gatcha.application.security.service.AuthenticationService;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.security.core.AuthenticationException;
import java.util.Optional;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
public class AuthenticationTest {
    @Mock
    AuthenticationService authenticationService;
    @Mock
    UserRepo userRepo;

    @Test
    public void givenAnAuthenticationRequest_DetectWrongPassword(){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("email", "pass");
        AuthenticationController authController = new AuthenticationController(authenticationService, userRepo);

        doReturn(Optional.of(new User())).when(userRepo).findByEmail("email");
        doThrow(new AuthenticationException("test") {}).when(authenticationService).verifyAuthenticationRequest(authenticationRequest);
        ResponseEntity<?> result = authController.verifyAndCreateAuthToken(authenticationRequest);

        assertEquals(UNAUTHORIZED, result.getStatusCode());
        APIResponse apiResponse = (APIResponse) result.getBody();
        assertEquals(UNAUTHORIZED.value(), apiResponse.getStatusCode());
        assertEquals("Wrong Password", apiResponse.getMessage());

    }

    @Test
    public void givenAnAuthenticationRequest_DetectEmailNotFound(){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("email@email.com", "pass");
        AuthenticationController authController = new AuthenticationController(authenticationService, userRepo);

        doReturn(Optional.empty()).when(userRepo).findByEmail("email@email.com");
        ResponseEntity<?> result = authController.verifyAndCreateAuthToken(authenticationRequest);

        assertEquals(UNAUTHORIZED, result.getStatusCode());
        APIResponse apiResponse = (APIResponse) result.getBody();
        assertEquals(UNAUTHORIZED.value(), apiResponse.getStatusCode());
        assertEquals("The Email email@email.com does not exist", apiResponse.getMessage());
    }

    @Test
    public void givenAuthenticationRequest_GetToken(){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("email", "pass");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("testJWT", "email");

        AuthenticationController authController = new AuthenticationController(authenticationService, userRepo);

        doReturn(Optional.of(new User())).when(userRepo).findByEmail("email");
        doReturn(authenticationResponse).when(authenticationService).createAuthToken(authenticationRequest);
        ResponseEntity<?> result = authController.verifyAndCreateAuthToken(authenticationRequest);

        assertEquals(OK, result.getStatusCode());
        AuthenticationResponse body = (AuthenticationResponse)result.getBody();
        assertEquals("testJWT", body.getJwt());
        assertEquals("email", body.getEmail());
    }

}
