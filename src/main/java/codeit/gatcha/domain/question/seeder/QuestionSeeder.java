package codeit.gatcha.domain.question.seeder;

import codeit.gatcha.domain.answer.entity.Answer;
import codeit.gatcha.domain.question.service.QuestionCreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component @Log4j2 @RequiredArgsConstructor
public class QuestionSeeder implements CommandLineRunner {
    private final QuestionCreationService questionCreationService;

    @Override
    public void run(String... args){
        Set<Answer> answers = new HashSet<>(Arrays.asList(
                new Answer("Telephone call"),
                new Answer("Email"),
                new Answer("Whatsapp")));

        questionCreationService.createQuestionWithAnswers("How do you prefer to be contacted?", answers);
    }
}
