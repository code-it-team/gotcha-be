package codeit.gatcha.api.security.controller;

import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.security.dto.AuthenticationRequest;
import codeit.gatcha.api.security.dto.SignOutRequestDto;
import codeit.gatcha.api.security.service.AuthenticationApiService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Getter
public class AuthenticationController {
    private final AuthenticationApiService authenticationApiService;

    @PostMapping("/authenticate")
    public ResponseEntity<APIResponse> verifyAndCreateAuthToken(@RequestBody AuthenticationRequest authenticationRequest)
    {
        return authenticationApiService.verifyAndCreateAuthToken(authenticationRequest);
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<APIResponse> createAccessTokenFromRefreshToken(HttpServletRequest servletRequest)
    {
        return authenticationApiService.createAccessTokenFromRefreshToken(servletRequest);
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signout(@RequestBody SignOutRequestDto signOutRequestDto)
    {
        return authenticationApiService.signout(signOutRequestDto);
    }

}
