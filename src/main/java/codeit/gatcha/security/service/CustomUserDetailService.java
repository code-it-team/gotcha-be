package codeit.gatcha.security.service;

import codeit.gatcha.security.CustomUserDetails;
import codeit.gatcha.user.repo.UserRepo;
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
