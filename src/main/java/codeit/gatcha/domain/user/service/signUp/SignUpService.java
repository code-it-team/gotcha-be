package codeit.gatcha.domain.user.service.signUp;

import codeit.gatcha.application.global.DTO.SingleMessageResponse;
import codeit.gatcha.application.security.entity.ConfirmationToken;
import codeit.gatcha.application.security.repo.AuthorityRepo;
import codeit.gatcha.application.security.repo.ConfirmationTokenRepo;
import codeit.gatcha.domain.user.DTO.SignUpDTO;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<SingleMessageResponse> confirmUserAccount(String confirmationToken) {
        return confirmationTokenRepo.
                findByConfirmationToken(confirmationToken).
                map(this::activateUserAccount).
                orElseGet(() -> createTokenNotFoundResponse(confirmationToken));
    }

    private ResponseEntity<SingleMessageResponse> createTokenNotFoundResponse(String confirmationToken) {
        return new ResponseEntity<>(new SingleMessageResponse(String.format("The token %s isn't found", confirmationToken)), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> createEmailAlreadyInUseMessage(User user) {
        return ResponseEntity.
                status(HttpStatus.CONFLICT).
                body(new SingleMessageResponse(String.format("The email %s is already in use", user.getEmail())));
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

    private ResponseEntity<SingleMessageResponse> activateUserAccount(ConfirmationToken confirmationToken) {
        User user = confirmationToken.getUser();
        enableUserAccount(user);
        return ResponseEntity.ok().body(new SingleMessageResponse(String.format("%s account has been activated", user.getEmail())));
    }

    private void enableUserAccount(User user) {
        user.setEnabled(true);
        userRepo.save(user);
    }
}
