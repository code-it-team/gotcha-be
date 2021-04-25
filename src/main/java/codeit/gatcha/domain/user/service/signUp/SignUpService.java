package codeit.gatcha.domain.user.service.signUp;

import codeit.gatcha.application.global.DTO.APIResponse;
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

import static org.springframework.http.HttpStatus.*;

@Service @RequiredArgsConstructor
public class SignUpService {
    private final UserRepo userRepo;
    private final AuthorityRepo authorityRepo;
    private final EmailConfirmationService confirmationService;
    private final ConfirmationTokenRepo confirmationTokenRepo;

    public ResponseEntity<APIResponse> signUpAndSendConfirmationEmail(SignUpDTO signUpDTO){
        ResponseEntity<APIResponse> userResponseEntity = signUp(signUpDTO);
        boolean newUserIsAdded = userResponseEntity.getStatusCode().is2xxSuccessful();

        if(newUserIsAdded)
            confirmationService.createAndSendConfirmationTokenToUser(getUserFromResponse(userResponseEntity));

        return userResponseEntity;
    }

    private User getUserFromResponse(ResponseEntity<APIResponse> userResponseEntity) {
        return (User) userResponseEntity.getBody().getResponse();
    }

    public ResponseEntity<APIResponse> signUp(SignUpDTO signUpDTO) {
        return userRepo.
                findByEmail(signUpDTO.getEmail()).
                map(this::createEmailAlreadyInUseMessage).
                orElseGet(() -> createNewUser(signUpDTO));
    }

    public ResponseEntity<APIResponse> confirmUserAccount(String confirmationToken) {
        return confirmationTokenRepo.
                findByConfirmationToken(confirmationToken).
                map(this::activateUserAccount).
                orElseGet(() -> createTokenNotFoundResponse(confirmationToken));
    }

    private ResponseEntity<APIResponse> createTokenNotFoundResponse(String confirmationToken) {
        return new ResponseEntity<>(new APIResponse(String.format("The token %s isn't found", confirmationToken), NOT_FOUND.value()), NOT_FOUND);
    }

    private ResponseEntity<APIResponse> createEmailAlreadyInUseMessage(User user) {
        return ResponseEntity.
                status(CONFLICT).
                body(new APIResponse(String.format("The email %s is already in use", user.getEmail()), CONFLICT.value()));
    }

    private ResponseEntity<APIResponse> createNewUser(SignUpDTO signUpDTO) {
        User newUser = createUserFromSignUpDTO(signUpDTO);
        userRepo.save(newUser);
        return ResponseEntity.status(CREATED).body(new APIResponse(newUser, CREATED.value()));
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

    private ResponseEntity<APIResponse> activateUserAccount(ConfirmationToken confirmationToken) {
        User user = confirmationToken.getUser();
        enableUserAccount(user);
        return ResponseEntity.ok().body(new APIResponse(String.format("%s account has been activated", user.getEmail()), OK.value()));
    }

    private void enableUserAccount(User user) {
        user.setEnabled(true);
        userRepo.save(user);
    }
}
