package codeit.gatcha.domain.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data @AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {
    @NotBlank(message = "Email can't be blank")
    private String email;
    @NotBlank(message = "Password can't be blank")
    private String password;
}
