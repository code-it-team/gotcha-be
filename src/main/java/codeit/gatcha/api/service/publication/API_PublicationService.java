package codeit.gatcha.api.service.publication;

import codeit.gatcha.api.DTO.Publication.PublicationLinkDTO;
import codeit.gatcha.api.DTO.Publication.PublishedQuestionDTO;
import codeit.gatcha.api.DTO.Publication.PublishedQuestionsDTO;
import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.common.user.service.UserSessionService;
import codeit.gatcha.domain.answer.repo.AnswerRepo;
import codeit.gatcha.domain.answer.service.AnswerFetchService;
import codeit.gatcha.domain.publication.entity.Publication;
import codeit.gatcha.domain.publication.repo.PublicationRepo;
import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor @Service
public class API_PublicationService {
    private final AnswerFetchService answerFetchService;
    private final PublicationRepo publicationRepo;
    private final UserSessionService userSessionService;
    private final AnswerRepo answerRepo;

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

    public ResponseEntity<APIResponse> getPublicationLink() {
        GatchaUser user = userSessionService.getCurrentLoggedInUser();
        Optional<Publication> optional = publicationRepo.findByGatchaUserAndPublishedIsTrue(user);

        if (optional.isEmpty())
            return generateBadRequest("This user hasn't published answers yet");
        else
            return ResponseEntity.ok
                    (new APIResponse(new PublicationLinkDTO(optional.get()), OK.value(), "success"));
    }

    public ResponseEntity<APIResponse> getPublishedAnswersByLink(String link) {
        Optional<Publication> publication = publicationRepo.findByLinkUniqueString(link);

        if (publication.isEmpty())
            return generateBadRequest("No such link exists");
        else{
            List<PublishedQuestionDTO> publishedQuestionDTOS = getUserAnswersAndCreatePublishedAnswersDTO(publication);
            return ResponseEntity.ok(new APIResponse(new PublishedQuestionsDTO(publishedQuestionDTOS), OK.value(), "success"));
        }

    }

    private List<PublishedQuestionDTO> getUserAnswersAndCreatePublishedAnswersDTO(Optional<Publication> publication) {
        return answerRepo.
                findByUser(publication.get().getGatchaUser()).
                stream().
                map(PublishedQuestionDTO::new).
                collect(Collectors.toList());
    }
}
