package codeit.gatcha.security.repo;

import codeit.gatcha.security.entity.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepo extends CrudRepository<Authority, Integer> {
}
