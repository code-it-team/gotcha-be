package codeit.gatcha.domain.user.repo;

import codeit.gatcha.domain.user.entity.GatchaUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<GatchaUser, Integer> {
    Optional<GatchaUser> findByEmail(String email);
}
