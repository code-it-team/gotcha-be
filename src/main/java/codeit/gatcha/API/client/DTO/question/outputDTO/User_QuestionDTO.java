package codeit.gatcha.API.client.DTO.question.outputDTO;

import codeit.gatcha.domain.question.entity.Question;
import lombok.Getter;

@Getter
public class User_QuestionDTO {
    private final String body;
    private final Integer id;
    private final String answer;

    public User_QuestionDTO(Question question, String answer){
        this.body = question.getBody();
        this.id = question.getId();
        this.answer = answer;
    }
}
