package codeit.gatcha.domain.user.service.signUp;

import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public boolean emailIsUsed(String email){
        return userRepo.findByEmail(email).isPresent();
    }
}
