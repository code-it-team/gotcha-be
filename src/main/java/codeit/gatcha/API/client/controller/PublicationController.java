package codeit.gatcha.API.client.controller;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.API.client.service.publication.API_PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PublicationController {
    private final API_PublicationService publicationService;

    @PostMapping("answers/publish")
    public ResponseEntity<APIResponse> publishAnswers() {
        return publicationService.publishAnswers();
    }

    @GetMapping("answers/getLink")
    public ResponseEntity<APIResponse> getPublicationLinkOfUser() {
        return publicationService.getPublicationLink();
    }
}
