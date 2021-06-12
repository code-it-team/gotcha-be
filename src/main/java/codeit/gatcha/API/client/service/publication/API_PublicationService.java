package codeit.gatcha.API.client.service.publication;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.application.user.service.UserSessionService;
import codeit.gatcha.domain.answer.service.AnswerFetchService;
import codeit.gatcha.domain.publication.entity.Publication;
import codeit.gatcha.domain.publication.repo.PublicationRepo;
import codeit.gatcha.domain.publication.service.PublicationService;
import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor @Service
public class API_PublicationService {
    private final AnswerFetchService answerFetchService;
    private final PublicationRepo publicationRepo;
    private final UserSessionService userSessionService;

    public ResponseEntity<APIResponse> publishAnswers() {
        GatchaUser user = userSessionService.getCurrentLoggedInUser();

        if (answerFetchService.currentUserHasntAnsweredAllQuestions(user))
            return generateBadRequest("Please answer all questions before publishing");

        else if (publicationRepo.findByGatchaUserAndPublishedIsTrue(user).isPresent())
            return generateBadRequest("User has already published the answers");

        else return createNewPublication(user);
    }

    private ResponseEntity<APIResponse> createNewPublication(GatchaUser user) {
        publicationRepo.save(new Publication(user, new Date(), true));
        return ResponseEntity.ok(new APIResponse(OK.value(), "The answers have been published"));
    }

    private ResponseEntity<APIResponse> generateBadRequest(String body) {
        return ResponseEntity.
                badRequest().
                body(new APIResponse(BAD_REQUEST.value(), body));
    }
}
