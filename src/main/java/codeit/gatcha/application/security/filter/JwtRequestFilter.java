package codeit.gatcha.application.security.filter;

import codeit.gatcha.application.security.service.CustomUserDetailService;
import codeit.gatcha.application.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final CustomUserDetailService customUserDetailsService;
    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final Optional<Cookie> jwtCookie = getJwtCookie(request);

        String username = null;
        String jwt = null;

        if (jwtCookie.isPresent()) {
            jwt = jwtCookie.get().getValue();
            username = jwtService.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
            setAuthenticationInSecurityContext(request, username, jwt);

        chain.doFilter(request, response);
    }

    private Optional<Cookie> getJwtCookie(HttpServletRequest request) {
        if (request.getCookies() == null )
            return Optional.empty();

        else return Arrays.stream(request.getCookies()).
                filter(c -> c.getName().equals("jwt")).
                findAny();
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
