package codeit.gatcha.API.admin.question.controller;

import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.API.DTO.question.inputDTO.NewQuestionWithAnswers_DTO;
import codeit.gatcha.API.DTO.question.outputDTO.QuestionsDTO;
import codeit.gatcha.API.service.question.API_QuestionDeletionService;
import codeit.gatcha.API.service.question.API_QuestionFetchService;
import codeit.gatcha.domain.question.service.QuestionCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController @RequiredArgsConstructor
public class QuestionController_Admin {
    private final QuestionCreationService questionCreationService;
    private final API_QuestionFetchService api_questionFetchService;
    private final API_QuestionDeletionService api_questionDeletionService;

    @PostMapping("/admin/createQuestion")
    public ResponseEntity<APIResponse> createNewQuestion(@RequestBody NewQuestionWithAnswers_DTO newQuestion){
        questionCreationService.createQuestionWithAnswers(newQuestion.getQuestionBody(), newQuestion.getAnswers());
        return ResponseEntity.ok(new APIResponse(OK.value(), "The Question has been created successfully"));
    }

    @GetMapping("/admin/questions")
    public ResponseEntity<APIResponse> getAllValidQuestions(){
        QuestionsDTO questionsDTO = api_questionFetchService.getAllValidQuestions_DTO();
        APIResponse apiResponse = new APIResponse(questionsDTO, OK.value(), "success");
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/admin/question/delete")
    public ResponseEntity<APIResponse> invalidateQuestionByIdOrThrow(@RequestBody int id) {
        return api_questionDeletionService.invalidateQuestionById(id);
    }
}
