package codeit.gatcha.api.service.question;

import codeit.gatcha.api.DTO.question.outputDTO.Admin_QuestionDTO;
import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.domain.question.service.QuestionUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Service @RequiredArgsConstructor
public class API_QuestionUpdateService {
    private final QuestionUpdateService questionUpdateService;

    public ResponseEntity<APIResponse> updateQuestionById(Admin_QuestionDTO questionDTO) {
        try{
            questionUpdateService.updateQuestion(questionDTO.getId(), questionDTO.getBody());
            return ResponseEntity.ok(new APIResponse(OK.value(), "Question has been successfully updated"));
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new APIResponse(NOT_FOUND.value(), "Question Not Found"));
        }
    }
}
