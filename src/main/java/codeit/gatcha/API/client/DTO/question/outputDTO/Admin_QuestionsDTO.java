package codeit.gatcha.API.client.DTO.question.outputDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class Admin_QuestionsDTO {
    private List<Admin_QuestionDTO> questions;
}
