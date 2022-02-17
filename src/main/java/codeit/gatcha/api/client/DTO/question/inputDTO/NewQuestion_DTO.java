package codeit.gatcha.api.client.DTO.question.inputDTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data @NoArgsConstructor
public class NewQuestion_DTO {
    @NotBlank(message = "Question body can't be empty")
    private String questionBody;
}
