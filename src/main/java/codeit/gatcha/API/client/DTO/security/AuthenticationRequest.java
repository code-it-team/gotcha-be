package codeit.gatcha.API.client.DTO.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {
    @NotBlank(message = "Email can't be blank")
    private String email;
    @NotBlank(message = "Password can't be blank")
    private String password;
}
