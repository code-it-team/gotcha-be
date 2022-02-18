package codeit.gatcha.api.security.service;

import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CustomUserDetails implements UserDetails
{
    private final String userName;
    private final String password;
    private final boolean enabled;
    private final List<GrantedAuthority> authorities = new ArrayList<>();

    public CustomUserDetails(GatchaUser user)
    {
        this.userName = user.getEmail();
        this.password = user.getPassword();
        this.enabled = user.isEnabled();
        this.authorities.add(new SimpleGrantedAuthority(user.getAuthority().getRole()));
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
        return this.userName;
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
