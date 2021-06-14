package codeit.gatcha.domain.publication.repo;

import codeit.gatcha.domain.publication.entity.Publication;
import codeit.gatcha.domain.user.entity.GatchaUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicationRepo extends CrudRepository<Publication, Integer> {
    Optional<Publication> findByGatchaUserAndPublishedIsTrue(GatchaUser user);
    Optional<Publication> findByLinkUniqueString(String link);
}
