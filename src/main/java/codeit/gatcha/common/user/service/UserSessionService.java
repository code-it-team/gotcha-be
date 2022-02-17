package codeit.gatcha.common.user.service;

import codeit.gatcha.api.security.CustomUserDetails;
import codeit.gatcha.domain.user.entity.GatchaUser;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;

@Service @RequiredArgsConstructor
public class UserSessionService {
    private final UserRepo userRepo;

    public GatchaUser getCurrentLoggedInUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.
                findByEmail(userDetails.getUsername()).
                orElseThrow(EntityNotFoundException::new);
    }
}
