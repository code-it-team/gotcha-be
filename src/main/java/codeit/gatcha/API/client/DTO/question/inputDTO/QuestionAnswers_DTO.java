package codeit.gatcha.API.client.DTO.question.inputDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor @Getter
public class QuestionAnswers_DTO {
    private final List<QuestionAnswer_DTO> questionAnswers;

    public QuestionAnswers_DTO() {
        this.questionAnswers = new ArrayList<>();
    }
}
