package codeit.gatcha.api.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService
{
    @Value("${jwt.secretkey}")
    private String SECRET_KEY;

    public String createToken(UserDetails userDetails, Optional<Integer> expirationDuration) {

        if (expirationDuration.isPresent())
            return createTokenWithNoExpirationDate(userDetails)
                    .setExpiration(new Date(System.currentTimeMillis() + expirationDuration.get()))
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
        else
            return createTokenWithNoExpirationDate(userDetails)
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean checkTokenValidityUsingUserName(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean tokenIsValid(String token)
    {
        extractAllClaims(token);
        return !isTokenExpired(token);
    }

    public Optional<String> extractJWTFromAuthorizationHeader(HttpServletRequest servletRequest){
        final String authorizationHeader = servletRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
            return Optional.empty();

        return Optional.of(authorizationHeader.substring(7));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private JwtBuilder createTokenWithNoExpirationDate(UserDetails userDetails)
    {
        return Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()));
    }
}
