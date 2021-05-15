package codeit.gatcha.API.client.controller;

import codeit.gatcha.API.client.DTO.APIResponse;
import codeit.gatcha.API.client.DTO.question.outputDTO.QuestionsDTO;
import codeit.gatcha.API.client.service.question.API_QuestionFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController @RequiredArgsConstructor
public class QuestionController {
    private final API_QuestionFetchService api_questionFetchService;

    @GetMapping("/questions")
    public ResponseEntity<APIResponse> getAllValidQuestions(){
        QuestionsDTO questionsDTO = api_questionFetchService.getAllValidQuestions_DTO();
        APIResponse apiResponse = new APIResponse(questionsDTO, OK.value(), "success");
        return ResponseEntity.ok(apiResponse);
    }
}
