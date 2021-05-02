package codeit.gatcha.domain.user.service.userQuestion;

import codeit.gatcha.domain.question.entity.Question;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.question.service.QuestionFetchService;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import codeit.gatcha.domain.user.service.UserFetchingService;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class UserQuestionsService {
    private final QuestionRepo questionRepo;
    private final UserRepo userRepo;
    private final UserFetchingService userFetchingService;
    private final QuestionFetchService questionFetchService;

    public void checkUserAndAddAllExistingQuestionsToUser(Integer userId){
        User user = userFetchingService.getUserByIdOrThrow(userId);
        addAllExistingQuestionsToUser(user);
    }

    private void addAllExistingQuestionsToUser(User user) {
        Iterable<Question> allQuestions = questionRepo.findAll();
        user.getQuestions().addAll(Sets.newHashSet(allQuestions));
        userRepo.save(user);
    }

    public void checkQuestionExistenceAndAddToUser(Integer userId, int questionId) {
        Question question = questionFetchService.getQuestionByIdOrThrow(questionId);
        User user = userFetchingService.getUserByIdOrThrow(userId);

        addQuestionToUser(question, user);
    }

    private void addQuestionToUser(Question question, User user) {
        user.getQuestions().add(question);
        userRepo.save(user);
    }
}
