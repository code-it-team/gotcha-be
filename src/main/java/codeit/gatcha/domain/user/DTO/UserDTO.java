package codeit.gatcha.domain.user.DTO;

import codeit.gatcha.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserDTO {
    private final Integer id;
    private final String email;
    public UserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
    }
}
