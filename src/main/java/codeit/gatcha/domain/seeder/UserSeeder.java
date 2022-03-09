package codeit.gatcha.domain.seeder;

import codeit.gatcha.domain.user.entity.Authority;
import codeit.gatcha.domain.user.entity.GatchaUser;
import codeit.gatcha.domain.user.repo.IAuthorityRepo;
import codeit.gatcha.domain.user.repo.IUserRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@Profile("dev")
public class UserSeeder implements CommandLineRunner {
    IUserRepo IUserRepo;
    IAuthorityRepo authorityRepo;

    @Autowired
    UserSeeder(IUserRepo IUserRepo, IAuthorityRepo authorityRepo){
        this.IUserRepo = IUserRepo;
        this.authorityRepo = authorityRepo;
    }

    @Override
    public void run(String... args){
        log.info("Trying To Adding New Users.....");

        Authority adminAuthority = addAuthorityIfNotFoundOrGet("ROLE_ADMIN");
        Authority userAuthority = addAuthorityIfNotFoundOrGet("ROLE_USER");

        addUserIfNotFoundOrGet("jalil.jarjanazy@gmail.com", "testPass", adminAuthority);
        addUserIfNotFoundOrGet("hazem.alabiad@gmail.com", "testPass", adminAuthority);
        addUserIfNotFoundOrGet("test.test@gmail.com", "testPass", userAuthority);
    }

    private Authority addAuthorityIfNotFoundOrGet(String role){
        return authorityRepo.
                findByRole(role).
                orElseGet(() -> authorityRepo.save(Authority.builder().role(role).build()));
    }

    private void addUserIfNotFoundOrGet(String email, String password, Authority authority){
        IUserRepo.
                findByEmail(email).
                orElseGet(() -> IUserRepo.save(new GatchaUser(email, password, true, authority)));
    }

}
