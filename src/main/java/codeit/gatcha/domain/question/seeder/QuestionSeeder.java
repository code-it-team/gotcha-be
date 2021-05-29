package codeit.gatcha.domain.question.seeder;

import codeit.gatcha.domain.question.repo.QuestionRepo;
import codeit.gatcha.domain.question.service.QuestionCreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component @Log4j2 @RequiredArgsConstructor
public class QuestionSeeder implements CommandLineRunner {
    private final QuestionRepo questionRepo;
    private final QuestionCreationService questionCreationService;

    @Override
    public void run(String... args){
        questionRepo.
                findQuestionsByBody("How do you prefer to be contacted?").
                orElseGet(() -> questionCreationService.createQuestion("How do you prefer to be contacted?"));
    }
}
