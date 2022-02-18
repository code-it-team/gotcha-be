package codeit.gatcha.api.security.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer>
{
    Optional<RefreshToken> findByValue(String value);

    void deleteByValue(String value);
}
