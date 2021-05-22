package codeit.gatcha.API.client.controller;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.API.client.DTO.security.AuthenticationRequest;
import codeit.gatcha.API.client.DTO.security.AuthenticationResponse;
import codeit.gatcha.API.client.service.security.AuthenticationService;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/isSignedIn")
    public ResponseEntity<APIResponse> userIsSignedIn(@RequestParam String email, HttpServletRequest httpServletRequest) {
        return authService.userIsSignedIn(email, httpServletRequest);
    }

    private ResponseEntity<APIResponse> verifyAndCreateAuthToken(AuthenticationRequest authRequest, HttpServletResponse response) {
        authService.verifyAuthenticationRequest(authRequest);

        AuthenticationResponse authToken = authService.createAuthToken(authRequest);

        return ResponseEntity.ok(new APIResponse(authToken, OK.value(),"Welcome!"));
    }

    private void createAuthTokenAndAddHttpOnlyCookie(HttpServletResponse response, AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authToken = authService.createAuthToken(authenticationRequest);

        Cookie cookie = new Cookie("jwt", authToken.getJwt());

        //cookie.setHttpOnly(true);

        response.addCookie(cookie);
    }

    private String getWrongEmailMessage(AuthenticationRequest authenticationRequest) {
        return String.format("The Email %s does not exist", authenticationRequest.getEmail());
    }

    private boolean emailIsntFound(AuthenticationRequest authenticationRequest) {
        return userRepo.findByEmail(authenticationRequest.getEmail()).isEmpty();
    }


}
