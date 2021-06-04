package codeit.gatcha.API.client.DTO.question.inputDTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class QuestionAnswer_DTO {
    private final Integer questionId;
    private final String answer;
}
