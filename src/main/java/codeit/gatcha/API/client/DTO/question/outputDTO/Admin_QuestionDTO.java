package codeit.gatcha.API.client.DTO.question.outputDTO;

import codeit.gatcha.domain.question.entity.Question;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor
public class Admin_QuestionDTO {
    private final String body;
    private final Integer id;

    public Admin_QuestionDTO(Question question){
        this.body = question.getBody();
        this.id = question.getId();
    }
}
