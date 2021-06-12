package codeit.gatcha.API.client.service.publication;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.domain.answer.service.AnswerFetchService;
import codeit.gatcha.domain.publication.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequiredArgsConstructor
public class API_PublicationService {
    private final PublicationService publicationService;
    private final AnswerFetchService answerFetchService;

    public ResponseEntity<APIResponse> publishAnswers() {
        if (answerFetchService.currentUserHasntAnsweredAllQuestions())
            return ResponseEntity.
                    badRequest().
                    body(new APIResponse(BAD_REQUEST.value(), "Please answer all questions before publishing"));

        return null;

    }
}
