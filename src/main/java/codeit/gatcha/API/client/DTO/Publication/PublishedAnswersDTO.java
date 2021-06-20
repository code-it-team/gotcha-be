package codeit.gatcha.API.client.DTO.Publication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter
public class PublishedAnswersDTO {
    List<PublishedAnswerDTO> publishedAnswers;
}
