package codeit.gatcha.domain.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data @AllArgsConstructor
@NoArgsConstructor
public class SignUpDTO {
    private String email;
    private String password;
}
