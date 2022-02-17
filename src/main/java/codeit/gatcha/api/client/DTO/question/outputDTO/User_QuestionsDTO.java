package codeit.gatcha.api.client.DTO.question.outputDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor
public class User_QuestionsDTO {
    private List<User_QuestionDTO> questions;
    private boolean published;
}
