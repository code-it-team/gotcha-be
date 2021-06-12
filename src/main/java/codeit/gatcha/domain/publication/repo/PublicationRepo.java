package codeit.gatcha.domain.publication.repo;

import codeit.gatcha.domain.publication.entity.Publication;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PublicationRepo extends CrudRepository<Publication, Integer> {
}
