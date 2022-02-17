package codeit.gatcha.domain.answer.service;

import codeit.gatcha.domain.answer.repo.AnswerRepo;
import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class AnswerFetchService {
    private final AnswerRepo answerRepo;
    private final QuestionRepo questionRepo;

    public boolean currentUserHasntAnsweredAllQuestions(GatchaUser loggedInUser) {
        return questionRepo.
                findQuestionsByValidTrue().
                stream().
                map(q -> answerRepo.findByQuestionAndUser(q, loggedInUser)).
                anyMatch(Optional::isEmpty);
    }
}
