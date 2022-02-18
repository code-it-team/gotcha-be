package codeit.gatcha.domain.user.service.signUp;

import codeit.gatcha.domain.user.repo.IUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class UserService {
    private final IUserRepo IUserRepo;

    public boolean emailIsUsed(String email){
        return IUserRepo.findByEmail(email).isPresent();
    }
}
