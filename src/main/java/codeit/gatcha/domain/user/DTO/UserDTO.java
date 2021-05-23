package codeit.gatcha.domain.user.DTO;

import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.Getter;

@Getter
public class UserDTO {
    private final Integer id;
    private final String email;
    public UserDTO(GatchaUser user){
        this.id = user.getId();
        this.email = user.getEmail();
    }
}
