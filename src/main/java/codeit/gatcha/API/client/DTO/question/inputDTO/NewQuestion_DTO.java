package codeit.gatcha.API.client.DTO.question.inputDTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewQuestion_DTO {
    @NotBlank(message = "Question body can't be empty")
    private String questionBody;
}
