package codeit.gatcha.api.DTO.Publication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter
public class PublishedQuestionsDTO {
    List<PublishedQuestionDTO> questions;
}
