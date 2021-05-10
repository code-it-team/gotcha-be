package codeit.gatcha.API.DTO;

import lombok.Data;

import java.util.List;

@Data
public class NewQuestionWithAnswers_DTO {
    private String questionBody;
    private List<String> answers;
}
