package codeit.gatcha.userTest;

import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import codeit.gatcha.domain.user.service.signUp.UserQuestionsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class UserQuestionServiceTest {
    @Mock
    QuestionRepo questionRepo;
    @Mock
    UserRepo userRepo;

    @Test
    void givenAUser_AddAllExistingQuestionsToUser(){
        UserQuestionsService userQuestionsService = new UserQuestionsService(questionRepo, userRepo);

        Question question1 = new Question(1, "q1", null);
        Question question2 = new Question(2, "q2", null);
        doReturn(Arrays.asList(question1, question2)).when(questionRepo).findAll();
        doAnswer(returnsFirstArg()).when(userRepo).save(any());

        User user = new User();
        userQuestionsService.addAllExistingQuestionsToUser(user);

        Iterator<Question> questions = user.getQuestions().iterator();

        assertEquals(2, user.getQuestions().size());
        assertEquals("q1", questions.next().getBody());
        assertEquals("q2", questions.next().getBody());
    }
}
