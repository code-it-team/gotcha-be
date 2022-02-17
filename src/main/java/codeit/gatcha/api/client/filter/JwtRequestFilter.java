package codeit.gatcha.api.client.filter;

import codeit.gatcha.api.security.service.CustomUserDetailService;
import codeit.gatcha.api.client.service.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private final CustomUserDetailService customUserDetailsService;
    private final JwtService jwtService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try{
            extractAndSetCredentials(request);
            chain.doFilter(request, response);
        }catch (ExpiredJwtException | MalformedJwtException e ){
            String jwt = String.format("JWT %s is invalid", request.getHeader("Authorization").substring(7));
            logger.info(jwt);
            response.getWriter().write(jwt);
            response.setStatus(BAD_REQUEST.value());
        }
    }

    private void extractAndSetCredentials(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        String email = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            email = jwtService.extractEmail(jwt);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null)
            setAuthenticationInSecurityContext(request, email, jwt);
    }

    private void setAuthenticationInSecurityContext(HttpServletRequest request, String username, String jwt) {
        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

        if (jwtService.validateToken(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

}
