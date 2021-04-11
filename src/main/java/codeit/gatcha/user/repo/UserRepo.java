package codeit.gatcha.user.repo;

import codeit.gatcha.user.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
