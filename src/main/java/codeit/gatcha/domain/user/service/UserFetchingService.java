package codeit.gatcha.domain.user.service;

import codeit.gatcha.domain.user.entity.GatchaUser;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service @RequiredArgsConstructor
public class UserFetchingService {
    private final UserRepo userRepo;

    public GatchaUser getUserByIdOrThrow(Integer userId) {
        return userRepo.
                findById(userId).
                orElseThrow(() -> new EntityNotFoundException("No such user"));
    }

}
