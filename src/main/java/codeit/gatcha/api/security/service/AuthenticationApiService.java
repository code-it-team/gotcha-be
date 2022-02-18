package codeit.gatcha.api.security.service;

import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.security.dto.AuthenticationRequest;
import codeit.gatcha.api.security.dto.AuthenticationResponse;
import codeit.gatcha.api.security.dto.SignOutRequestDto;
import codeit.gatcha.api.security.refreshtoken.RefreshTokenService;
import codeit.gatcha.domain.user.entity.GatchaUser;
import codeit.gatcha.domain.user.repo.IUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import static codeit.gatcha.api.response.ResponseFactory.createResponse;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@Service
public class AuthenticationApiService
{
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailService;
    private final IUserRepo IUserRepo;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public ResponseEntity<Void> signout(SignOutRequestDto signOutRequestDto)
    {
        refreshTokenService.deleteByValue(signOutRequestDto.getRefreshToken());
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<APIResponse> verifyAndCreateAuthToken(AuthenticationRequest authenticationRequest)
    {
        try{

            verifyAuthenticationRequest(authenticationRequest);

            return ResponseEntity.ok(createAuthToken(authenticationRequest));

        }catch (AuthenticationException e){
            return createResponse("Wrong Username or Password", UNAUTHORIZED);

        }catch (EntityNotFoundException re){
            return createResponse(String.format("The username %s isn't found", authenticationRequest.getUserName()), BAD_REQUEST);
        }
    }

    public ResponseEntity<APIResponse> createAccessTokenFromRefreshToken(HttpServletRequest servletRequest)
    {
        Optional<String> refreshTokenOptional = jwtService.extractJWTFromAuthorizationHeader(servletRequest);
        if (refreshTokenOptional.isEmpty())
            return createResponse("The refresh token doesn't exist", BAD_REQUEST);

        String refreshToken = refreshTokenOptional.get();
        if (jwtService.tokenIsValid(refreshToken) && refreshTokenService.refreshTokenExists(refreshToken)){
            return ResponseEntity.ok(createAccessTokenFromRefreshToken(refreshToken));
        }
        else
            return createResponse("The refresh token is invalid", BAD_REQUEST);
    }

    private APIResponse createAccessTokenFromRefreshToken(String refreshToken)
    {
        CustomUserDetails userDetails = CustomUserDetails.builder().userName(jwtService.extractUsername(refreshToken)).build();
        String newAccessToken = createAccessToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(newAccessToken);
        return new APIResponse(authenticationResponse, OK.value(), "success");
    }

    private APIResponse createAuthToken(AuthenticationRequest authenticationRequest) {
        UserDetails userDetails =  customUserDetailService.loadUserByUsername(authenticationRequest.getUserName());

        GatchaUser user = IUserRepo.
                findByEmail(userDetails.getUsername()).
                orElseThrow(EntityNotFoundException::new);

        String refreshToken = jwtService.createToken(userDetails, Optional.empty());

        refreshTokenService.saveNewRefreshToken(refreshToken, user);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(refreshToken, createAccessToken(userDetails), user.getEmail());

        return new APIResponse(authenticationResponse, OK.value(), "success");
    }

    // if this method runs successfully it means that authentication done successfully
    private void verifyAuthenticationRequest(AuthenticationRequest ar) throws AuthenticationException
    {
        var authentication = new UsernamePasswordAuthenticationToken(ar.getUserName(), ar.getPassword());
        authenticationManager.authenticate(authentication);
    }

    private String createAccessToken(UserDetails userDetails)
    {
        return jwtService.createToken(userDetails, Optional.of(1000 * 60 * 30));
    }
}
