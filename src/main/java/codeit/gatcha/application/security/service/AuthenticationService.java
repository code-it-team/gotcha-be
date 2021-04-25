package codeit.gatcha.application.security.service;

import codeit.gatcha.application.security.DTO.AuthenticationRequest;
import codeit.gatcha.application.security.DTO.AuthenticationResponse;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService customUserDetailService;
    private final UserRepo userRepo;
    private final JwtService jwtService;

    public AuthenticationResponse createAuthToken(AuthenticationRequest authenticationRequest) {
        UserDetails userDetails =  customUserDetailService.loadUserByUsername(authenticationRequest.getEmail());

        User user = userRepo.
                findByEmail(userDetails.getUsername()).
                orElseThrow(() -> new RuntimeException(""));

        String jwt = jwtService.generateToken(userDetails);

        return new AuthenticationResponse(jwt, user.getEmail());
    }
    // if this method runs successfully it means that authentication done successfully
    public void verifyAuthenticationRequest(AuthenticationRequest ar) {
        var authentication = new UsernamePasswordAuthenticationToken(ar.getEmail(), ar.getPassword());
        authenticationManager.authenticate(authentication);
    }
}
