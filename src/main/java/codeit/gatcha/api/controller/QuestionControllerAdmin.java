package codeit.gatcha.api.controller;

import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.DTO.question.inputDTO.NewQuestion_DTO;
import codeit.gatcha.api.DTO.question.outputDTO.Admin_QuestionDTO;
import codeit.gatcha.api.DTO.question.outputDTO.Admin_QuestionsDTO;
import codeit.gatcha.api.service.question.API_QuestionDeletionService;
import codeit.gatcha.api.service.question.API_QuestionFetchService;
import codeit.gatcha.api.service.question.API_QuestionUpdateService;
import codeit.gatcha.domain.question.service.QuestionCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.OK;

@RestController @RequiredArgsConstructor
public class QuestionControllerAdmin {
    private final QuestionCreationService questionCreationService;
    private final API_QuestionFetchService api_questionFetchService;
    private final API_QuestionDeletionService api_questionDeletionService;
    private final API_QuestionUpdateService api_questionUpdateService;

    @PostMapping("/admin/createQuestion")
    public ResponseEntity<APIResponse> createNewQuestion(@RequestBody @Validated NewQuestion_DTO newQuestion){
        questionCreationService.createQuestion(newQuestion.getQuestionBody());
        return ResponseEntity.ok(new APIResponse(OK.value(), "The Question has been created successfully"));
    }

    @GetMapping("/admin/questions")
    public ResponseEntity<APIResponse> getAllValidQuestions(){
        Admin_QuestionsDTO questionsDTO = api_questionFetchService.getAllValidQuestions_DTO();
        APIResponse apiResponse = new APIResponse(questionsDTO, OK.value(), "success");
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/admin/question/delete/{id}")
    public ResponseEntity<APIResponse> invalidateQuestionByIdOrThrow(@PathVariable int id) {
        return api_questionDeletionService.invalidateQuestionById(id);
    }

    @PutMapping("/admin/question/update")
    public ResponseEntity<APIResponse> updateQuestionById(@RequestBody @Validated Admin_QuestionDTO questionDTO){
        return api_questionUpdateService.updateQuestionById(questionDTO);
    }
}
