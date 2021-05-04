package codeit.gatcha.API.controller;

import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.application.security.DTO.AuthenticationRequest;
import codeit.gatcha.application.security.DTO.AuthenticationResponse;
import codeit.gatcha.application.security.service.AuthenticationService;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController @RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;
    private final UserRepo userRepo;

    @PostMapping("/signin")
    public ResponseEntity<?> checkEmailAndVerify(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response){
        try{
            if (emailIsntFound(authenticationRequest))
                return ResponseEntity.
                        status(UNAUTHORIZED).
                        body(new APIResponse(UNAUTHORIZED.value(), getWrongEmailMessage(authenticationRequest)));

            return verifyAndCreateAuthToken(authenticationRequest, response);
        }catch (AuthenticationException e){
            return ResponseEntity.status(UNAUTHORIZED).body(new APIResponse(UNAUTHORIZED.value(), "Wrong Password"));
        }
    }

    private ResponseEntity<APIResponse> verifyAndCreateAuthToken(AuthenticationRequest authRequest, HttpServletResponse response) {
        authService.verifyAuthenticationRequest(authRequest);
        
        createAuthTokenAndAddHttpOnlyCookie(response, authRequest);

        return ResponseEntity.ok(new APIResponse(OK.value(), "Welcome!"));
    }

    private void createAuthTokenAndAddHttpOnlyCookie(HttpServletResponse response, AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authToken = authService.createAuthToken(authenticationRequest);

        Cookie cookie = new Cookie("jwt", authToken.getJwt());

        cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }

    private String getWrongEmailMessage(AuthenticationRequest authenticationRequest) {
        return String.format("The Email %s does not exist", authenticationRequest.getEmail());
    }

    private boolean emailIsntFound(AuthenticationRequest authenticationRequest) {
        return userRepo.findByEmail(authenticationRequest.getEmail()).isEmpty();
    }

}
