package codeit.gatcha.api.controller;

import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.service.publication.API_PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PublicationController {
    private final API_PublicationService publicationService;

    @PostMapping("answers/publish")
    public ResponseEntity<APIResponse> publishAnswers()
    {
        return publicationService.publishAnswers();
    }

    @GetMapping("answers/link")
    public ResponseEntity<APIResponse> getPublicationLinkOfUser()
    {
        return publicationService.getPublicationLink();
    }

    @GetMapping("answers/published/{link}")
    public ResponseEntity<APIResponse> getPublishedAnswersByLink(@PathVariable String link)
    {
        return publicationService.getPublishedAnswersByLink(link);
    }
}
