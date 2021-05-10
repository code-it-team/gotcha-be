package codeit.gatcha.API.admin.question.controller;

import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.API.DTO.NewQuestionWithAnswers_DTO;
import codeit.gatcha.domain.question.service.QuestionCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.HttpStatus.OK;

@RestController @RequiredArgsConstructor
public class QuestionController_Admin {
    private final QuestionCreationService questionCreationService;

    @PostMapping("/admin/createQuestion")
    public ResponseEntity<APIResponse> createNewQuestion(@RequestBody NewQuestionWithAnswers_DTO newQuestion){
        questionCreationService.createQuestionWithAnswers(newQuestion.getQuestionBody(), newQuestion.getAnswers());

        return ResponseEntity.ok(new APIResponse(OK.value(), "The Question has been created successfully"));
    }

}
