package codeit.gatcha.api.security.service;

import codeit.gatcha.domain.user.repo.IUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService
{
    private final IUserRepo IUserRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
    {
        return IUserRepo
                .findByEmail(userName)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("The username %s wasn't found", userName)));
    }
}
