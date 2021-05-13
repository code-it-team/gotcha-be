package codeit.gatcha.authenticationTest;

import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.API.controller.AuthenticationController;
import codeit.gatcha.API.service.security.AuthenticationService;
import codeit.gatcha.API.service.security.JwtService;
import codeit.gatcha.domain.user.repo.UserRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import javax.servlet.http.Cookie;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class SignInCheckTest {
    @Mock
    UserRepo userRepo;
    @Mock
    MockHttpServletRequest mockHttpServletRequest;
    @Mock
    JwtService jwtService;

    @Test
    public void givenAuthenticationRequest_ForSignInCheck_DetectNoCookieFound(){
        AuthenticationService authenticationService = new AuthenticationService(null, null, null, new JwtService());
        AuthenticationController authController = new AuthenticationController(authenticationService, userRepo);

        doReturn(null).when(mockHttpServletRequest).getCookies();

        ResponseEntity<APIResponse> result =  authController.userIsSignedIn("email", mockHttpServletRequest);

        assertEquals(OK, result.getStatusCode());
        assertEquals(OK.value(), result.getBody().getStatusCode());
        assertEquals("No Cookie is provided", result.getBody().getMessage());
    }

    @Test
    public void givenAuthenticationRequest_ForSignInCheck_DetectEmailNotMatchingJWT(){
        AuthenticationService authenticationService = new AuthenticationService(null, null, null, jwtService);
        AuthenticationController authController = new AuthenticationController(authenticationService, userRepo);

        Cookie cookie = new Cookie("jwt", "testToken");

        doReturn(Optional.of(cookie)).when(jwtService).getJwtCookie(mockHttpServletRequest);
        doReturn("email1").when(jwtService).extractEmail("testToken");

        ResponseEntity<APIResponse> result =  authController.userIsSignedIn("email2", mockHttpServletRequest);

        assertEquals(OK, result.getStatusCode());
        assertEquals(OK.value(), result.getBody().getStatusCode());
        assertEquals("The current loggedIn email isn't the one sent", result.getBody().getMessage());
    }

    @Test
    public void givenAuthenticationRequest_ForSignInCheck_DetectUserIsSignedInAndValid(){
        AuthenticationService authenticationService = new AuthenticationService(null, null, null, jwtService);
        AuthenticationController authController = new AuthenticationController(authenticationService, userRepo);

        Cookie cookie = new Cookie("jwt", "testToken");

        doReturn(Optional.of(cookie)).when(jwtService).getJwtCookie(mockHttpServletRequest);
        doReturn("email1").when(jwtService).extractEmail("testToken");

        ResponseEntity<APIResponse> result =  authController.userIsSignedIn("email1", mockHttpServletRequest);

        assertEquals(OK, result.getStatusCode());
        assertEquals(OK.value(), result.getBody().getStatusCode());
        assertEquals("The user is loggedIn", result.getBody().getMessage());
    }


}
