package codeit.gatcha.authenticationTest;

import codeit.gatcha.api.client.filter.JwtRequestFilter;
import codeit.gatcha.api.security.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
public class SignInCheckTest {
    @Mock
    MockHttpServletRequest mockHttpServletRequest;
    @Mock
    JwtService jwtService;

    @Test
    public void givenJWT_DetectNotValid() throws Exception{
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(null,jwtService);
        doReturn("Bearer jwt").when(mockHttpServletRequest).getHeader("Authorization");
        doThrow(new ExpiredJwtException(null ,null, null)).when(jwtService).extractEmail("jwt");

        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

        jwtRequestFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, null);

        assertEquals(BAD_REQUEST.value(), mockHttpServletResponse.getStatus());
        assertEquals("JWT jwt is invalid", mockHttpServletResponse.getContentAsString());

    }


}
