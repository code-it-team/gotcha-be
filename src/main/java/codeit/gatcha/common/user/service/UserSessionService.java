package codeit.gatcha.common.user.service;

import codeit.gatcha.api.security.service.CustomUserDetails;
import codeit.gatcha.domain.user.entity.GatchaUser;
import codeit.gatcha.domain.user.repo.IUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;

@Service @RequiredArgsConstructor
public class UserSessionService {
    private final IUserRepo IUserRepo;

    public GatchaUser getCurrentLoggedInUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return IUserRepo.
                findByEmail(userDetails.getUsername()).
                orElseThrow(EntityNotFoundException::new);
    }
}
