package codeit.gatcha.API.service.security;

import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.API.DTO.security.AuthenticationRequest;
import codeit.gatcha.API.DTO.security.AuthenticationResponse;
import codeit.gatcha.application.security.service.CustomUserDetailService;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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

    public ResponseEntity<APIResponse> userIsSignedIn(String email, HttpServletRequest httpServletRequest) {
        Optional<Cookie> jwtCookie = jwtService.getJwtCookie(httpServletRequest);

        return jwtCookie.
                map(c -> checkEmailValidity(c, email)).
                orElse(createUnauthorizedResponseWithMessage("No Cookie is provided"));
    }

    private ResponseEntity<APIResponse> createUnauthorizedResponseWithMessage(String message) {
        return ResponseEntity.
                status(UNAUTHORIZED).
                body(new APIResponse(UNAUTHORIZED.value(), message));
    }

    private ResponseEntity<APIResponse> checkEmailValidity(Cookie cookie, String email) {
        String emailFromJWT = jwtService.extractEmail(cookie.getValue());

        if (!emailFromJWT.equals(email))
            return createUnauthorizedResponseWithMessage("The current loggedIn email isn't the one sent");

        else
            return ResponseEntity.ok(new APIResponse(OK.value(), "The user is loggedIn"));
    }
}
