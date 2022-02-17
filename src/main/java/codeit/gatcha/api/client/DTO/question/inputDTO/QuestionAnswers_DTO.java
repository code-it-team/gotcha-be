package codeit.gatcha.api.client.DTO.question.inputDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@Getter @NoArgsConstructor
public class QuestionAnswers_DTO {
    @Size(min = 1)
    @Valid
    private List<QuestionAnswer_DTO> questionAnswers;
}
