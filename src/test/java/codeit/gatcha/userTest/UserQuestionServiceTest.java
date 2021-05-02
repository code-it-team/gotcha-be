package codeit.gatcha.userTest;

import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.question.service.QuestionFetchService;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import codeit.gatcha.domain.user.service.UserFetchingService;
import codeit.gatcha.domain.user.service.userQuestion.UserQuestionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    QuestionFetchService questionFetchService;
    UserFetchingService userFetchingService;
    UserQuestionsService userQuestionsService;

    @BeforeEach
    void setup(){
        questionFetchService = new QuestionFetchService(questionRepo);
        userFetchingService = new UserFetchingService(userRepo);
        userQuestionsService = new UserQuestionsService(questionRepo, userRepo, userFetchingService, questionFetchService);
    }

    @Test
    void givenAUser_AddAllExistingQuestionsToUser(){
        Question question1 = new Question(1, "q1", null);
        Question question2 = new Question(2, "q2", null);
        User user = User.builder().id(33).questions(new HashSet<>()).build();

        doReturn(Arrays.asList(question1, question2)).when(questionRepo).findAll();
        doAnswer(returnsFirstArg()).when(userRepo).save(any());
        doReturn(Optional.of(user)).when(userRepo).findById(33);

        userQuestionsService.checkUserAndAddAllExistingQuestionsToUser(33);

        Iterator<Question> questions = user.getQuestions().iterator();

        assertEquals(2, user.getQuestions().size());
        assertEquals("q1", questions.next().getBody());
        assertEquals("q2", questions.next().getBody());
    }

    @Test
    void givenAUserToAddAllQuestionTo_DetectNotFound(){
        doReturn(Optional.empty()).when(userRepo).findById(22);
        Throwable throwable = assertThrows(EntityNotFoundException.class,
                () -> userQuestionsService.checkUserAndAddAllExistingQuestionsToUser(22));
        assertEquals("No such user", throwable.getMessage());
    }

    @Test
    void givenAUserAndAQuestion_AddANewQuestion(){
        Question question = new Question();
        User user = new User();

        doReturn(Optional.of(question)).when(questionRepo).findById(166);
        doReturn(Optional.of(user)).when(userRepo).findById(55);
        doReturn(user).when(userRepo).save(user);

        userQuestionsService.checkQuestionExistenceAndAddToUser(55, 166);

        assertEquals(1, user.getQuestions().size());
        assertEquals(question, user.getQuestions().iterator().next());
    }

    @Test
    void givenAUserAndQuestion_DetectQuestionNotFound(){
        doReturn(Optional.empty()).when(questionRepo).findById(1234);
        Throwable throwable = assertThrows(EntityNotFoundException.class,
                () -> userQuestionsService.
                        checkQuestionExistenceAndAddToUser(123, 1234));
        assertEquals("No such question", throwable.getMessage());
    }

    @Test
    void givenAUserAndQuestion_DetectUserNotFound(){
        doReturn(Optional.of(new Question())).when(questionRepo).findById(99);
        doReturn(Optional.empty()).when(userRepo).findById(66);
        Throwable throwable = assertThrows(EntityNotFoundException.class,
                () -> userQuestionsService.
                        checkQuestionExistenceAndAddToUser(66, 99));
        assertEquals("No such user", throwable.getMessage());
    }
}
