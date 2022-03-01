package codeit.gatcha.api.security.service;

import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.security.dto.AuthenticationRequest;
import codeit.gatcha.api.security.dto.AuthenticationResponse;
import codeit.gatcha.api.security.dto.SignOutRequestDto;
import codeit.gatcha.api.security.entity.RefreshToken;
import codeit.gatcha.api.security.repo.RefreshTokenRepository;
import codeit.gatcha.domain.user.entity.GatchaUser;
import codeit.gatcha.domain.user.repo.IUserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthenticationApiServiceTest
{
    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private IUserRepo IUserRepo;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Captor
    ArgumentCaptor<RefreshToken> refreshTokenArgumentCaptor;

    private AuthenticationApiService authenticationApiService;

    @BeforeEach
    public void setup()
    {
        RefreshTokenService refreshTokenService = new RefreshTokenService(refreshTokenRepository);
        authenticationApiService = new AuthenticationApiService(authenticationManager, customUserDetailsService, IUserRepo, jwtService, refreshTokenService);
    }

    @Test
    public void givenARefreshToken_WhenValidAndExistInDB_ThenCreateAnAccessToken()
    {
        when(jwtService.extractJWTFromAuthorizationHeader(any(HttpServletRequest.class))).thenReturn(Optional.of("refreshToken"));

        when(jwtService.tokenIsValid("refreshToken")).thenReturn(true);

        when(jwtService.extractUsername("refreshToken")).thenReturn("userName");

        when(jwtService.createToken(any(UserDetails.class), any(Optional.class))).thenReturn("accessToken");

        when(refreshTokenRepository.findByValue("refreshToken")).thenReturn(Optional.of(new RefreshToken()));

        ResponseEntity<APIResponse> response = authenticationApiService.createAccessTokenFromRefreshToken(new MockHttpServletRequest());

        AuthenticationResponse authenticationResponse = (AuthenticationResponse) response.getBody().getBody();

        assertThat("accessToken").isEqualTo(authenticationResponse.getAccessToken());
        assertThat(authenticationResponse.getRefreshToken()).isNull();
        assertThat(authenticationResponse.getUserName()).isNull();
    }

    @Test
    public void givenARefreshToken_WhenValidButDosentExistInDB_ThenCreateAnAccessToken()
    {
        when(jwtService.extractJWTFromAuthorizationHeader(any(HttpServletRequest.class))).thenReturn(Optional.of("refreshToken"));

        when(jwtService.tokenIsValid("refreshToken")).thenReturn(true);

        when(jwtService.extractUsername("refreshToken")).thenReturn("userName");

        when(jwtService.createToken(any(UserDetails.class), any(Optional.class))).thenReturn("accessToken");

        when(refreshTokenRepository.findByValue("refreshToken")).thenReturn(Optional.empty());

        ResponseEntity<APIResponse> response = authenticationApiService.createAccessTokenFromRefreshToken(new MockHttpServletRequest());

        APIResponse errorResponse = response.getBody();

        assertThat(HttpStatus.BAD_REQUEST.value()).isEqualTo(errorResponse.getStatusCode());
        assertThat("The refresh token is invalid").isEqualTo(errorResponse.getMessage());
    }

    @Test
    public void givenAnAuthenticationRequest_WhenUserNotFound_DetectException()
    {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("userName", "password");

        when(customUserDetailsService.loadUserByUsername("userName")).thenReturn(CustomUserDetails.builder().userName("userName").build());

        when(IUserRepo.findByEmail("userName")).thenReturn(Optional.empty());
        ResponseEntity<APIResponse> response = authenticationApiService.verifyAndCreateAuthToken(authenticationRequest);

        APIResponse errorResponse = response.getBody();
        assertThat(errorResponse.getMessage()).isEqualTo("The username userName isn't found");
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void givenAnAuthenticationRequest_WhenUserIsFound_CreateAuthToken()
    {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("userName", "password");

        CustomUserDetails userDetails = CustomUserDetails.builder().userName("userName").build();

        when(customUserDetailsService.loadUserByUsername("userName")).thenReturn(userDetails);

        GatchaUser user = GatchaUser.builder().email("userName").id(1).build();

        when(IUserRepo.findByEmail("userName")).thenReturn(Optional.of(user));

        when(jwtService.createToken(userDetails, Optional.empty())).thenReturn("refreshToken");

        when(jwtService.createToken(userDetails, Optional.of(1000 * 60 * 30))).thenReturn("accessToken");

        ResponseEntity<APIResponse> response = authenticationApiService.verifyAndCreateAuthToken(authenticationRequest);

        AuthenticationResponse authenticationResponse = (AuthenticationResponse) response.getBody().getBody();

        verify(refreshTokenRepository).save(refreshTokenArgumentCaptor.capture());

        RefreshToken savedRefreshToken = refreshTokenArgumentCaptor.getValue();

        assertThat(authenticationResponse.getRefreshToken()).isEqualTo("refreshToken");
        assertThat(authenticationResponse.getAccessToken()).isEqualTo("accessToken");
        assertThat(authenticationResponse.getUserName()).isEqualTo("userName");

        assertThat(savedRefreshToken.getValue()).isEqualTo("refreshToken");
        assertThat(savedRefreshToken.getUserId()).isEqualTo(1);
        assertThat(savedRefreshToken.getCreationDate()).isNotNull();
    }

    @Test
    public void givenASignoutRequest_ThenRemoveRefreshTokenFromDB()
    {
        SignOutRequestDto signOutRequestDto = new SignOutRequestDto("refresh token");

        ResponseEntity<Void> response = authenticationApiService.signout(signOutRequestDto);

        verify(refreshTokenRepository).deleteByValue("refresh token");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
