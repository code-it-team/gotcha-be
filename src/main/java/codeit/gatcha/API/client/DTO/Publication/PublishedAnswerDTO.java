package codeit.gatcha.API.client.DTO.Publication;

import codeit.gatcha.domain.answer.entity.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Getter
public class PublishedAnswerDTO {
    private String question;
    private String answer;

    public PublishedAnswerDTO(Answer answer) {
        this.question = answer.getQuestion().getBody();
        this.answer = answer.getBody();
    }
}
