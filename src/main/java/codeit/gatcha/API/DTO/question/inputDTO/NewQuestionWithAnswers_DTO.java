package codeit.gatcha.API.DTO.question.inputDTO;

import lombok.Data;
import java.util.List;

@Data
public class NewQuestionWithAnswers_DTO {
    private String questionBody;
    private String answer;
}
