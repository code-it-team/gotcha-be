package codeit.gatcha.application.security.controller;

import codeit.gatcha.application.global.DTO.APIResponse;
import codeit.gatcha.application.security.DTO.AuthenticationRequest;
import codeit.gatcha.application.security.DTO.AuthenticationResponse;
import codeit.gatcha.application.security.service.CustomUserDetailService;
import codeit.gatcha.application.security.service.JwtService;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController @RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService customUserDetailService;
    private final UserRepo userRepo;
    private final JwtService jwtService;

    @PostMapping("/signin")
    public ResponseEntity<?> verifyAndCreateAuthToken(@RequestBody AuthenticationRequest authenticationRequest){
        try{
            verifyAuthenticationRequest(authenticationRequest);
            return createAuthToken(authenticationRequest);
        }catch (AuthenticationException e){
            return ResponseEntity.status(UNAUTHORIZED).body(new APIResponse("Wrong Email or Password", UNAUTHORIZED.value()));
        }
    }

    private ResponseEntity<AuthenticationResponse> createAuthToken(AuthenticationRequest authenticationRequest) {
        UserDetails userDetails =  customUserDetailService.loadUserByUsername(authenticationRequest.getEmail());

        User user = userRepo.
                findByEmail(userDetails.getUsername()).
                orElseThrow(() -> new RuntimeException(""));

        String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt, user.getEmail()));
    }
    // if this method runs successfully it means that authentication done successfully
    private void verifyAuthenticationRequest(AuthenticationRequest ar) {
        var authentication = new UsernamePasswordAuthenticationToken(ar.getEmail(), ar.getPassword());
        authenticationManager.authenticate(authentication);
    }

}
