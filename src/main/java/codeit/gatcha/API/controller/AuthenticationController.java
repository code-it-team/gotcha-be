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
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController @RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;
    private final UserRepo userRepo;

    @PostMapping("/signin")
    public ResponseEntity<?> verifyAndCreateAuthToken(@RequestBody AuthenticationRequest authenticationRequest){
        try{
            if (emailIsntFound(authenticationRequest))
                return ResponseEntity.status(UNAUTHORIZED).body(new APIResponse(UNAUTHORIZED.value(), "Wrong Email"));

            authService.verifyAuthenticationRequest(authenticationRequest);
            AuthenticationResponse authToken = authService.createAuthToken(authenticationRequest);
            return ResponseEntity.ok(authToken);
        }catch (AuthenticationException e){
            return ResponseEntity.status(UNAUTHORIZED).body(new APIResponse(UNAUTHORIZED.value(), "Wrong Password"));
        }
    }

    private boolean emailIsntFound(AuthenticationRequest authenticationRequest) {
        return userRepo.findByEmail(authenticationRequest.getEmail()).isEmpty();
    }

}
