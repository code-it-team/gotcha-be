package codeit.gatcha.user.service.signUp;

import codeit.gatcha.security.entity.ConfirmationToken;
import codeit.gatcha.security.repo.AuthorityRepo;
import codeit.gatcha.security.repo.ConfirmationTokenRepo;
import codeit.gatcha.user.DTO.SignUpDTO;
import codeit.gatcha.user.entity.User;
import codeit.gatcha.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class SingUpService {
    private final UserRepo userRepo;
    private final AuthorityRepo authorityRepo;
    private final EmailConfirmationService confirmationService;
    private final ConfirmationTokenRepo confirmationTokenRepo;

    public ResponseEntity<User> signUpAndSendConfirmationEmail(SignUpDTO signUpDTO){
        ResponseEntity<User> userResponseEntity = signUp(signUpDTO);
        boolean newUserIsAdded = userResponseEntity.getStatusCode().is2xxSuccessful();

        if(newUserIsAdded)
            confirmationService.createAndSendConfirmationTokenToUser(userResponseEntity.getBody());

        return userResponseEntity;
    }

    public ResponseEntity<User> signUp(SignUpDTO signUpDTO) {
        return userRepo.findByEmail(signUpDTO.getEmail()).
                map(this::createUsernameAlreadyTakenMessage).
                orElse(createNewUser(signUpDTO));
    }

    private ResponseEntity<User> createUsernameAlreadyTakenMessage(User user) {
        return ResponseEntity.
                status(HttpStatus.CONFLICT).
                body(user);
    }

    private ResponseEntity<User> createNewUser(SignUpDTO signUpDTO) {
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

    public ResponseEntity<?> confirmUserAccount(String confirmationToken) {
        return confirmationTokenRepo.
                findByConfirmationToken(confirmationToken).
                map(this::activateUserAccount).
                orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<?> activateUserAccount(ConfirmationToken confirmationToken) {
        User user = confirmationToken.getUser();
        userRepo.
                findByEmail(user.getEmail()).
                ifPresent(this::enableUserAccount);

        return ResponseEntity.ok().build();
    }

    private void enableUserAccount(User user) {
        user.setEnabled(true);
        userRepo.save(user);
    }
}
