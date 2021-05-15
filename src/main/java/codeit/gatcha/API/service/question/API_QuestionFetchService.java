package codeit.gatcha.API.service.question;

import codeit.gatcha.API.DTO.question.outputDTO.AnswerDTO;
import codeit.gatcha.API.DTO.question.outputDTO.QuestionDTO;
import codeit.gatcha.API.DTO.question.outputDTO.QuestionsDTO;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class API_QuestionFetchService {
    private final QuestionRepo questionRepo;

    public QuestionsDTO getAllValidQuestions_DTO() {
        List<Question> questions = questionRepo.findQuestionsByValidTrue();
        List<QuestionDTO> questionDTOS = questions.
                stream().
                map(QuestionDTO::new).
                collect(Collectors.toList());
        return new QuestionsDTO(questionDTOS);
    }

}
