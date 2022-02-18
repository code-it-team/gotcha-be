package codeit.gatcha.domain.user.repo;

import codeit.gatcha.domain.user.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthorityRepo extends JpaRepository<Authority, Integer>
{
    Optional<Authority> findByRole(String role);
}
