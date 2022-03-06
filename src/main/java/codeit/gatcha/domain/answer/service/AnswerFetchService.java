package codeit.gatcha.domain.answer.service;

import codeit.gatcha.domain.answer.repo.IAnswerRepo;
import codeit.gatcha.domain.question.repo.IQuestionRepo;
import codeit.gatcha.domain.user.entity.GatchaUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class AnswerFetchService {
    private final IAnswerRepo answerRepo;
    private final IQuestionRepo questionRepo;

    public boolean currentUserHasntAnsweredAllQuestions(GatchaUser loggedInUser) {
        return questionRepo.
                findQuestionsByValidTrue().
                stream().
                map(q -> answerRepo.findByQuestionAndUser(q, loggedInUser)).
                anyMatch(Optional::isEmpty);
    }
}
