package codeit.gatcha.API.client.DTO.question.outputDTO;

import codeit.gatcha.domain.question.entity.Question;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data @RequiredArgsConstructor
public class Admin_QuestionDTO {
    @NotBlank(message = "Question can't be empty")
    private final String body;
    @NotBlank(message = "Question id can't be null")
    private final Integer id;

    public Admin_QuestionDTO(Question question){
        this.body = question.getBody();
        this.id = question.getId();
    }
}
