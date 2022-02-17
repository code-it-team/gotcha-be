package codeit.gatcha.authenticationTest;

import codeit.gatcha.api.client.DTO.APIResponse;
import codeit.gatcha.api.client.controller.AuthenticationController;
import codeit.gatcha.api.client.DTO.security.AuthenticationRequest;
import codeit.gatcha.api.client.DTO.security.AuthenticationResponse;
import codeit.gatcha.api.client.service.security.AuthenticationService;
import codeit.gatcha.domain.user.entity.GatchaUser;
import codeit.gatcha.domain.user.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.mock.web.MockHttpServletResponse;
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
    @Mock
    MockHttpServletResponse mockHttpServletResponse;

    @Test
    public void givenAnAuthenticationRequest_DetectWrongPassword(){
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("email", "pass");
        AuthenticationController authController = new AuthenticationController(authenticationService, userRepo);

        doReturn(Optional.of(new GatchaUser())).when(userRepo).findByEmail("email");
        doThrow(new AuthenticationException("test") {}).when(authenticationService).verifyAuthenticationRequest(authenticationRequest);
        ResponseEntity<?> result = authController.checkEmailAndVerify(authenticationRequest, null);

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
        ResponseEntity<?> result = authController.checkEmailAndVerify(authenticationRequest, null);

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

        doReturn(Optional.of(new GatchaUser())).when(userRepo).findByEmail("email");
        doReturn(authenticationResponse).when(authenticationService).createAuthToken(authenticationRequest);
        ResponseEntity<?> result = authController.checkEmailAndVerify(authenticationRequest, mockHttpServletResponse);

        assertEquals(OK, result.getStatusCode());
        APIResponse body = (APIResponse) result.getBody();
        AuthenticationResponse authResponse = (AuthenticationResponse) body.getBody();

        assertEquals("Welcome!", body.getMessage());
        assertEquals(OK.value(), body.getStatusCode());
        assertEquals("testJWT", authResponse.getJwt());
        assertEquals("email", authResponse.getEmail());
    }
}
