package codeit.gatcha.application.security.repo;

import codeit.gatcha.application.security.entity.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConfirmationTokenRepo extends CrudRepository<ConfirmationToken, Integer> {
    Optional<ConfirmationToken> findByConfirmationToken(String confirmationToken);
}
