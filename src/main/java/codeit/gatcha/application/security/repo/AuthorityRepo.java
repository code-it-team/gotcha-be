package codeit.gatcha.application.security.repo;

import codeit.gatcha.application.security.entity.Authority;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface AuthorityRepo extends CrudRepository<Authority, Integer> {
    Optional<Authority> findByRole(String role);
}
