package codeit.gatcha.API.client.service.question;

import codeit.gatcha.API.client.DTO.question.outputDTO.Admin_QuestionDTO;
import codeit.gatcha.API.client.DTO.question.outputDTO.Admin_QuestionsDTO;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class API_QuestionFetchService {
    private final QuestionRepo questionRepo;

    public Admin_QuestionsDTO getAllValidQuestions_DTO() {
        List<Question> questions = questionRepo.findQuestionsByValidTrue();
        List<Admin_QuestionDTO> questionDTOS = questions.
                stream().
                map(Admin_QuestionDTO::new).
                collect(Collectors.toList());
        return new Admin_QuestionsDTO(questionDTOS);
    }

}
