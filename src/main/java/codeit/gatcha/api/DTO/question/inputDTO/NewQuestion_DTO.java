package codeit.gatcha.api.DTO.question.inputDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NewQuestion_DTO {
    @NotBlank(message = "Question body can't be empty")
    private String questionBody;
}
