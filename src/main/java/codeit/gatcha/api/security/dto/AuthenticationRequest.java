package codeit.gatcha.api.security.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@RequiredArgsConstructor
@Getter
public class AuthenticationRequest implements Serializable
{
    @NotNull(message = "user name can't be null")
    private final String userName;
    @NotNull(message = "password can't be empty")
    private final String password;
}
