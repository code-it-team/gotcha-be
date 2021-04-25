package codeit.gatcha.API.service;

import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.application.security.service.ConfirmationTokenService;
import codeit.gatcha.domain.user.DTO.SignUpDTO;
import codeit.gatcha.domain.user.DTO.UserDTO;
import codeit.gatcha.domain.user.entity.User;
import codeit.gatcha.domain.user.service.signUp.EmailConfirmationService;
import codeit.gatcha.domain.user.service.signUp.SignUpService;
import codeit.gatcha.domain.user.service.signUp.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service @RequiredArgsConstructor
public class API_SignUpService {
    private final SignUpService signUpService;
    private final EmailConfirmationService confirmationService;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    public ResponseEntity<APIResponse> signUpAndSendConfirmationEmail(SignUpDTO signUpDTO) {
        if (userService.emailIsUsed(signUpDTO.getEmail()))
            return createEmailAlreadyInUseMessage(signUpDTO);
        else
            return createNewUserAndSendConfirmationEmail(signUpDTO);
    }

    private ResponseEntity<APIResponse> createNewUserAndSendConfirmationEmail(SignUpDTO signUpDTO) {
        User user = signUpService.createNewUser(signUpDTO);
        confirmationService.createAndSendConfirmationTokenToUser(user);
        return ResponseEntity.
                status(CREATED).
                body(new APIResponse(new UserDTO(user), CREATED.value()));
    }

    private ResponseEntity<APIResponse> createEmailAlreadyInUseMessage(SignUpDTO signUpDTO) {
        return ResponseEntity.
                status(CONFLICT).
                body(new APIResponse(String.format("The email %s is already in use", signUpDTO.getEmail()), CONFLICT.value()));
    }

    public ResponseEntity<APIResponse> confirmUserAccount(String confirmationToken) {
        if (confirmationTokenService.confirmationTokenExists(confirmationToken))
            return createTokenNotFoundResponse(confirmationToken);
        else
            return activateUserAccount(confirmationToken);
    }

    private ResponseEntity<APIResponse> activateUserAccount(String confirmationToken) {
        signUpService.activateUserAccount(confirmationToken);
        return ResponseEntity.ok().body(new APIResponse("The account has been activated", OK.value()));
    }

    private ResponseEntity<APIResponse> createTokenNotFoundResponse(String confirmationToken) {
        return new ResponseEntity<>(new APIResponse(String.format("The token %s isn't found", confirmationToken), NOT_FOUND.value()), NOT_FOUND);
    }
}
