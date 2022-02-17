package codeit.gatcha.api.security.repo;

import codeit.gatcha.api.security.entity.GatchaAuthority;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface AuthorityRepo extends CrudRepository<GatchaAuthority, Integer> {
    Optional<GatchaAuthority> findByRole(String role);
}
