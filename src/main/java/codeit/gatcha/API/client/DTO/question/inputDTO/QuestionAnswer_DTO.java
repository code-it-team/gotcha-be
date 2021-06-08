package codeit.gatcha.API.client.DTO.question.inputDTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor @Getter
public class QuestionAnswer_DTO {
    @NotBlank(message = "Question id can't be null")
    private final Integer questionId;
    @NotBlank(message = "Answer can't be empty")
    private final String answer;
}
