package codeit.gatcha.api.client.DTO.Publication;

import codeit.gatcha.domain.publication.entity.Publication;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class PublicationLinkDTO {
    private final String link;
    private final String publicationDate;

    public PublicationLinkDTO(Publication publication) {
        this.link = publication.getLinkUniqueString();
        this.publicationDate = publication.getPublicationDate().toString();
    }
}
