package codeit.gatcha.user.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor
public class SignUpDTO {
    private final String userName;
    private final String password;
}
