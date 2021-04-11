package codeit.gatcha.security;

import codeit.gatcha.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final String email;
    private final String password;
    private final boolean enabled;
    private final List<GrantedAuthority> authorities;


    public CustomUserDetails(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();

        authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getAuthority().getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
