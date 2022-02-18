package codeit.gatcha.api.controller;

import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.DTO.question.inputDTO.QuestionAnswers_DTO;
import codeit.gatcha.api.DTO.question.outputDTO.User_QuestionsDTO;
import codeit.gatcha.api.service.answer.API_AnswerSubmissionService;
import codeit.gatcha.api.service.question.API_QuestionFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.HttpStatus.OK;

@RestController @RequiredArgsConstructor
public class QuestionController {
    private final API_QuestionFetchService api_questionFetchService;
    private final API_AnswerSubmissionService api_answerSubmissionService;


    @GetMapping("/questions")
    public ResponseEntity<APIResponse> getAllValidQuestionsWithUserAnswers(){
        User_QuestionsDTO questionsDTO = api_questionFetchService.getAllValidQuestionsWithUserAnswers_DTO();
        APIResponse apiResponse = new APIResponse(questionsDTO, OK.value(), "success");
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/questions/submitAnswer")
    public ResponseEntity<APIResponse> submitAnswers(@RequestBody @Validated QuestionAnswers_DTO questionAnswers_dto){
        return api_answerSubmissionService.submitQuestionsAnswers(questionAnswers_dto);
    }
}
