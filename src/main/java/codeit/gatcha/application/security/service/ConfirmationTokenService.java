package codeit.gatcha.application.security.service;

import codeit.gatcha.application.security.repo.ConfirmationTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepo confirmationTokenRepo;

    public boolean confirmationTokenDoesntExist(String confirmationToken){
        return confirmationTokenRepo.findByConfirmationToken(confirmationToken).isEmpty();
    }
}
