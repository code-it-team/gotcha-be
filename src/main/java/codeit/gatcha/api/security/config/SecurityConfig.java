package codeit.gatcha.api.security.config;


import codeit.gatcha.api.security.controller.JwtRequestFilter;
import codeit.gatcha.api.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService customUserDetailService;
    private final JwtRequestFilter jwtRequestFilter;

    private final String[] ALLOWED_URLS = {
            "/authenticate", "/refreshToken","/h2-console/**", "/signup",
            "/v2/api-docs","/swagger-resources/**","/swagger-ui.html", "/webjars/**", "/configuration/ui"
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()// enable Cross Origin Resource Sharing which allows requests from outside the domain to be made to the server
                .and()
                .csrf().disable()// disable Cross Site Request Forgery, check here for details on why https://stackoverflow.com/questions/62696806/reason-to-disable-csrf-in-spring-boot
                .authorizeRequests()
                    // permit any request to access those URLs
                    .antMatchers(ALLOWED_URLS).permitAll()
                    // for the admin URLs only allow users with an ADMIN role to access it
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    // for the other URLs require authentication
                    .anyRequest().authenticated()
                .and()
                .sessionManagement()
                //this tells Spring to not run its default process creating sessions
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers().frameOptions().disable(); // check here for the reason https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Frame-Options
    }

    @Bean
    public PasswordEncoder getPasswordEncoder (){
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
