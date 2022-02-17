package codeit.gatcha.api.security.controller;

import codeit.gatcha.api.client.DTO.APIResponse;
import codeit.gatcha.api.client.DTO.security.AuthenticationRequest;
import codeit.gatcha.api.client.DTO.security.AuthenticationResponse;
import codeit.gatcha.api.security.service.AuthenticationApiService;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController @RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationApiService authService;
    private final UserRepo userRepo;

    @PostMapping("/signin")
    public ResponseEntity<?> checkEmailAndVerify(@RequestBody @Validated AuthenticationRequest authenticationRequest, HttpServletResponse response){
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

        AuthenticationResponse authToken = authService.createAuthToken(authRequest);

        return ResponseEntity.ok(new APIResponse(authToken, OK.value(),"Welcome!"));
    }

    private String getWrongEmailMessage(AuthenticationRequest authenticationRequest) {
        return String.format("The Email %s does not exist", authenticationRequest.getEmail());
    }

    private boolean emailIsntFound(AuthenticationRequest authenticationRequest) {
        return userRepo.findByEmail(authenticationRequest.getEmail()).isEmpty();
    }
}
