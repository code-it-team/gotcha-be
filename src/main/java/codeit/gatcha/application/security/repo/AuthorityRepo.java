package codeit.gatcha.application.security.repo;

import codeit.gatcha.application.security.entity.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepo extends CrudRepository<Authority, Integer> {
    Authority findByRole(String role);
}
