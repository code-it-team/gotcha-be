package codeit.gatcha.API.client.DTO.question.outputDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor @Data
public class QuestionsDTO {
    private final List<QuestionDTO> questions;
}
