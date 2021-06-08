package codeit.gatcha.API.client.DTO.question.inputDTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@RequiredArgsConstructor @Getter
public class QuestionAnswers_DTO {
    @Size(min = 1)
    @Valid
    private final List<QuestionAnswer_DTO> questionAnswers;
}
