package codeit.gatcha.API.service.question;

import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.domain.question.service.QuestionDeletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Service @RequiredArgsConstructor
public class API_QuestionDeletionService {
    private final QuestionDeletionService questionDeletionService;

    public ResponseEntity<APIResponse> invalidateQuestionById(int id){
        try{
            questionDeletionService.invalidateQuestionByIdOrThrow(id);
            return ResponseEntity.ok(new APIResponse(OK.value(), "Question Deleted"));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(NOT_FOUND.value(), "Question Not Found"));
        }
    }
}
