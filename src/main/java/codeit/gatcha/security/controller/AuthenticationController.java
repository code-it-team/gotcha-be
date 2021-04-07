package codeit.gatcha.security.controller;

import codeit.gatcha.security.DTO.AuthenticationRequest;
import codeit.gatcha.security.DTO.AuthenticationResponse;
import codeit.gatcha.security.service.CustomUserDetailService;
import codeit.gatcha.security.service.JwtService;
import codeit.gatcha.user.entity.User;
import codeit.gatcha.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService customUserDetailService;
    private final UserRepo userRepo;
    private final JwtService jwtService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> verifyAndCreateAuthToken(@RequestBody AuthenticationRequest authenticationRequest){
        try{
            verifyAuthenticationRequest(authenticationRequest);
            return createAuthToken(authenticationRequest);

        }catch (AuthenticationException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong Username or Password");
        }catch (RuntimeException re){
            return ResponseEntity.badRequest().body(String.format("The user %s isn't found", authenticationRequest.getUsername()));
        }
    }

    private ResponseEntity<AuthenticationResponse> createAuthToken(AuthenticationRequest authenticationRequest) {
        UserDetails userDetails =  customUserDetailService.loadUserByUsername(authenticationRequest.getUsername());

        User user = userRepo.
                findByUserName(userDetails.getUsername()).
                orElseThrow(() -> new RuntimeException(""));

        String jwt = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt, user.getUserName()));
    }
    // if this method runs successfully it means that authentication done successfully
    private void verifyAuthenticationRequest(AuthenticationRequest ar) {
        var authentication = new UsernamePasswordAuthenticationToken(ar.getUsername(), ar.getPassword());
        authenticationManager.authenticate(authentication);
    }

}
