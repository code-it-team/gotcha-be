package codeit.gatcha.api.client.DTO.question.outputDTO;

import codeit.gatcha.domain.question.entity.Question;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class User_QuestionDTO {
    private String body;
    private Integer id;
    private String answer;

    public User_QuestionDTO(Question question, String answer){
        this.body = question.getBody();
        this.id = question.getId();
        this.answer = answer;
    }
}
