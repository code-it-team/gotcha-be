package codeit.gatcha.security.repo;

import codeit.gatcha.security.entity.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AuthorityRepo extends CrudRepository<Authority, Integer> {
    Authority findByRole(String role);
}
