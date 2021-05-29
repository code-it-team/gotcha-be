package codeit.gatcha.API.client.DTO.question.outputDTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor @Getter
public class User_QuestionsDTO {
    private final List<User_QuestionDTO> questions;
}
