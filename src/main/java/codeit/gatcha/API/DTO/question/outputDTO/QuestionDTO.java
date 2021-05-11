package codeit.gatcha.API.DTO.question.outputDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Data @RequiredArgsConstructor
public class QuestionDTO {
    private final String body;
    private final List<AnswerDTO> answers;
}
