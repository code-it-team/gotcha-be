package codeit.gatcha.domain.user.service.signUp;

import codeit.gatcha.security.entity.ConfirmationToken;
import codeit.gatcha.security.repo.AuthorityRepo;
import codeit.gatcha.security.repo.ConfirmationTokenRepo;
import codeit.gatcha.domain.user.DTO.SignUpDTO;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class SignUpService {
    private final UserRepo userRepo;
    private final AuthorityRepo authorityRepo;
    private final EmailConfirmationService confirmationService;
    private final ConfirmationTokenRepo confirmationTokenRepo;

    public ResponseEntity<Object> signUpAndSendConfirmationEmail(SignUpDTO signUpDTO){
        ResponseEntity<Object> userResponseEntity = signUp(signUpDTO);
        boolean newUserIsAdded = userResponseEntity.getStatusCode().is2xxSuccessful();

        if(newUserIsAdded)
            confirmationService.createAndSendConfirmationTokenToUser((User) userResponseEntity.getBody());

        return userResponseEntity;
    }

    public ResponseEntity<Object> signUp(SignUpDTO signUpDTO) {
        return userRepo.
                findByEmail(signUpDTO.getEmail()).
                map(this::createEmailAlreadyInUseMessage).
                orElseGet(() -> createNewUser(signUpDTO));
    }

    public ResponseEntity<String> confirmUserAccount(String confirmationToken) {
        return confirmationTokenRepo.
                findByConfirmationToken(confirmationToken).
                map(this::activateUserAccount).
                orElseGet(() -> new ResponseEntity<>(String.format("The token %s isn't found", confirmationToken), HttpStatus.NOT_FOUND));
    }

    private ResponseEntity<Object> createEmailAlreadyInUseMessage(User user) {
        return ResponseEntity.
                status(HttpStatus.CONFLICT).
                body(String.format("The email %s is already in use", user.getEmail()));
    }

    private ResponseEntity<Object> createNewUser(SignUpDTO signUpDTO) {
        User newUser = createUserFromSignUpDTO(signUpDTO);
        userRepo.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    private User createUserFromSignUpDTO(SignUpDTO signUpDTO) {
        return User.
                builder().
                email(signUpDTO.getEmail()).
                password(signUpDTO.getPassword()).
                authority(authorityRepo.findByRole("ROLE_USER")).
                enabled(false).
                build();
    }

    private ResponseEntity<String> activateUserAccount(ConfirmationToken confirmationToken) {
        User user = confirmationToken.getUser();
        enableUserAccount(user);
        return ResponseEntity.ok().body(String.format("%s account has been activated", user.getEmail()));
    }

    private void enableUserAccount(User user) {
        user.setEnabled(true);
        userRepo.save(user);
    }
}
