package codeit.gatcha.domain.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {
    private String email;
    private String password;
}
