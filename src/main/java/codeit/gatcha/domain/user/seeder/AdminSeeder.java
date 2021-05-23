package codeit.gatcha.domain.user.seeder;

import codeit.gatcha.application.security.entity.Authority;
import codeit.gatcha.application.security.repo.AuthorityRepo;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;
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
        if (dbNotAlreadyPopulated())
            addAdmins();
        else
            log.info("DB Already Populated");
    }

    private boolean dbNotAlreadyPopulated() {
        return userRepo.
                findByEmail("jalil.jarjanazy@gmail.com").
                isEmpty();
    }

    private void addAdmins() {
        log.info("Adding new users.....");

        Authority adminAuthority = new Authority(new HashSet<>(), "ROLE_ADMIN");
        Authority userAuthority = new Authority(new HashSet<>(), "ROLE_USER");

        User jalil = new User("jalil.jarjanazy@gmail.com", "testPass", true, adminAuthority);
        User hazem = new User("hazem.alabiad@gmail.com", "testPass", true, adminAuthority);
        User testUser = new User("test.test@gmail.com", "testPass", true, userAuthority);

        authorityRepo.saveAll(Arrays.asList(adminAuthority, userAuthority));
        userRepo.saveAll(Arrays.asList(jalil, hazem, testUser));
    }
}
