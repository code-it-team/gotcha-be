package codeit.gatcha.api.DTO.question.inputDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@Getter @NoArgsConstructor
public class QuestionAnswers_DTO {
    @Size(min = 1, message = "Must at least have one answer question pair")
    @NotNull(message = "Must at least have one answer question pair")
    @Valid
    private List<QuestionAnswer_DTO> questionAnswers;
}
