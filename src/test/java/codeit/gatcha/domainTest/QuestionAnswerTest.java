package codeit.gatcha.domainTest;

import codeit.gatcha.application.user.service.UserSessionService;
import codeit.gatcha.domain.answer.entity.Answer;
import codeit.gatcha.domain.answer.repo.AnswerRepo;
import codeit.gatcha.domain.answer.service.AnswerFetchService;
import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.user.entity.GatchaUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class QuestionAnswerTest {
    @Mock
    QuestionRepo questionRepo;
    @Mock
    AnswerRepo answerRepo;

    @Test
    public void givenUser_DetectHasAnsweredAllQuestions(){
        AnswerFetchService answerFetchService = new AnswerFetchService(answerRepo, questionRepo);

        GatchaUser user = new GatchaUser();
        Question q1 = new Question("q1");
        Question q2 = new Question("q2");

        doReturn(Arrays.asList(q1, q2)).when(questionRepo).findQuestionsByValidTrue();

        doReturn(Optional.of(new Answer())).when(answerRepo).findByQuestionAndUser(q1, user);
        doReturn(Optional.of(new Answer())).when(answerRepo).findByQuestionAndUser(q2, user);

        boolean result = answerFetchService.currentUserHasntAnsweredAllQuestions(user);
        assertFalse(result);
    }

}
