package codeit.gatcha.API.client.service.question;

import codeit.gatcha.API.client.DTO.question.outputDTO.Admin_QuestionDTO;
import codeit.gatcha.API.client.DTO.question.outputDTO.Admin_QuestionsDTO;
import codeit.gatcha.API.client.DTO.question.outputDTO.User_QuestionDTO;
import codeit.gatcha.API.client.DTO.question.outputDTO.User_QuestionsDTO;
import codeit.gatcha.application.user.service.UserSessionService;
import codeit.gatcha.domain.answer.entity.Answer;
import codeit.gatcha.domain.answer.repo.AnswerRepo;
import codeit.gatcha.domain.publication.repo.PublicationRepo;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class API_QuestionFetchService {
    private final QuestionRepo questionRepo;
    private final AnswerRepo answerRepo;
    private final UserSessionService userSessionService;
    private final PublicationRepo publicationRepo;

    public Admin_QuestionsDTO getAllValidQuestions_DTO() {
        List<Question> questions = questionRepo.findQuestionsByValidTrue();
        List<Admin_QuestionDTO> questionDTOS = questions.
                stream().
                map(Admin_QuestionDTO::new).
                collect(Collectors.toList());
        return new Admin_QuestionsDTO(questionDTOS);
    }

    public User_QuestionsDTO getAllValidQuestionsWithUserAnswers_DTO(){
        List<Question> questions = questionRepo.findQuestionsByValidTrue();
        GatchaUser currentLoggedInUser = userSessionService.getCurrentLoggedInUser();

        List<User_QuestionDTO> questionDTOS = questions.
                stream().
                map(q -> new User_QuestionDTO(q, getUserAnswerToQuestion(q, currentLoggedInUser))).
                collect(Collectors.toList());

        return new User_QuestionsDTO(questionDTOS, answersArePublished(currentLoggedInUser));
    }

    private boolean answersArePublished(GatchaUser currentLoggedInUser) {
        return publicationRepo.
                findByGatchaUserAndPublishedIsTrue(currentLoggedInUser).
                isPresent();
    }

    private String getUserAnswerToQuestion(Question question, GatchaUser gatchaUser) {
       return answerRepo.
               findByQuestionAndUser(question, gatchaUser).
               map(Answer::getBody).
               orElse("");

    }

}
