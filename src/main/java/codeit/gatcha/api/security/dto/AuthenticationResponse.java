package codeit.gatcha.api.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthenticationResponse
{
    private String refreshToken;
    private String accessToken;
    private String userName;

    public AuthenticationResponse(String accessToken)
    {
        this.accessToken = accessToken;
    }
}
