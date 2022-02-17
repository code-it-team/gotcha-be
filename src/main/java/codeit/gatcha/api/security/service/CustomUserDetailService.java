package codeit.gatcha.api.security.service;

import codeit.gatcha.api.security.CustomUserDetails;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepo.
                findByEmail(userName).
                map(CustomUserDetails::new).
                orElseThrow(() -> new BadCredentialsException("Email or password are incorrect"));
    }
}
