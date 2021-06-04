package codeit.gatcha.API.client.service.answer;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.API.client.DTO.question.inputDTO.QuestionAnswers_DTO;
import codeit.gatcha.domain.answer.service.AnswerSubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor @Service
public class API_AnswerSubmissionService {
    private final AnswerSubmissionService answerSubmissionService;

    public ResponseEntity<APIResponse> submitQuestionsAnswers(QuestionAnswers_DTO questionAnswers_dto) {
        try {
            questionAnswers_dto.
                    getQuestionAnswers().
                    forEach(answerSubmissionService::checkAndSubmitAnswer);

            return ResponseEntity.ok(new APIResponse(null, OK.value(), "success"));
        }catch (EntityNotFoundException e){
            return ResponseEntity.badRequest().body(new APIResponse(null, BAD_REQUEST.value(), e.getMessage()));
        }
    }
}
