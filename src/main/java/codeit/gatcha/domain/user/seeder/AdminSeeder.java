package codeit.gatcha.domain.user.seeder;

import codeit.gatcha.application.security.entity.GatchaAuthority;
import codeit.gatcha.application.security.repo.AuthorityRepo;
import codeit.gatcha.domain.user.entity.GatchaUser;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component @Log4j2
public class AdminSeeder implements CommandLineRunner {
    UserRepo userRepo;
    AuthorityRepo authorityRepo;

    @Autowired
    AdminSeeder(UserRepo userRepo, AuthorityRepo authorityRepo){
        this.userRepo = userRepo;
        this.authorityRepo = authorityRepo;
    }

    @Override
    public void run(String... args){
        log.info("Trying To Adding New Users.....");

        GatchaAuthority adminAuthority = addAuthorityIfNotFoundOrGet("ROLE_ADMIN");
        GatchaAuthority userAuthority = addAuthorityIfNotFoundOrGet("ROLE_USER");

        addUserIfNotFoundOrGet("jalil.jarjanazy@gmail.com", "testPass", adminAuthority);
        addUserIfNotFoundOrGet("hazem.alabiad@gmail.com", "testPass", adminAuthority);
        addUserIfNotFoundOrGet("test.test@gmail.com", "testPass", userAuthority);
    }

    private GatchaAuthority addAuthorityIfNotFoundOrGet(String role){
        return authorityRepo.
                findByRole(role).
                orElseGet(() -> authorityRepo.save(new GatchaAuthority(new HashSet<>(), role)));
    }

    private void addUserIfNotFoundOrGet(String email, String password, GatchaAuthority authority){
        userRepo.
                findByEmail(email).
                orElseGet(() -> userRepo.save(new GatchaUser(email, password, true, authority)));
    }

}
