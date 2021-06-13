package codeit.gatcha.API.client.DTO.question.inputDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @NoArgsConstructor @AllArgsConstructor
public class QuestionAnswer_DTO {
    @NotNull(message = "Question id can't be null")
    private Integer questionId;
    @NotBlank(message = "Answer can't be empty")
    private String answer;
}
