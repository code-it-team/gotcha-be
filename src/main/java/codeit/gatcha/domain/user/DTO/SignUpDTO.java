package codeit.gatcha.domain.user.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor
public class SignUpDTO {
    private final String email;
    private final String password;
}
