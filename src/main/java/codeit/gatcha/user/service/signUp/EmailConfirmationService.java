package codeit.gatcha.user.service.signUp;

import codeit.gatcha.email.service.EmailSendingService;
import codeit.gatcha.security.entity.ConfirmationToken;
import codeit.gatcha.security.repo.ConfirmationTokenRepo;
import codeit.gatcha.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class EmailConfirmationService {
    private final ConfirmationTokenRepo confirmationTokenRepo;
    private final EmailSendingService emailSendingService;

    @Value("${server.rootAddress}")
    private String rootAddress;

    @Value("${spring.mail.username}")
    private String email;


    public void createAndSendConfirmationTokenToUser(User user){
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepo.save(confirmationToken);
        createAndSendConfirmationEmail(user, confirmationToken);
    }

    private void createAndSendConfirmationEmail(User user, ConfirmationToken confirmationToken) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom(email);
        mailMessage.setText(getConfirmationLink(confirmationToken));

        emailSendingService.sendEmail(mailMessage);
    }

    private String getConfirmationLink(ConfirmationToken confirmationToken) {
        return "To confirm your account, please click here: " +
                String.format("%s/confirm-account?token=%s", rootAddress, confirmationToken.getConfirmationToken());
    }

}
