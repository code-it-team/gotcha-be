package codeit.gatcha.api.client.DTO.Publication;

import codeit.gatcha.domain.answer.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Getter
public class PublishedQuestionDTO {
    private String body;
    private String answer;

    public PublishedQuestionDTO(Answer answer) {
        this.body = answer.getQuestion().getBody();
        this.answer = answer.getBody();
    }
}
