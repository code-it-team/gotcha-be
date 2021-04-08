package codeit.gatcha.user.service;

import codeit.gatcha.security.repo.AuthorityRepo;
import codeit.gatcha.user.DTO.SignUpDTO;
import codeit.gatcha.user.entity.User;
import codeit.gatcha.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final AuthorityRepo authorityRepo;

    public ResponseEntity<String> signUp(SignUpDTO signUpDTO) {
        return userRepo.findByUserName(signUpDTO.getUserName()).
                map(this::createUsernameAlreadyTakenMessage).
                orElse(createNewUser(signUpDTO));
    }

    private ResponseEntity<String> createUsernameAlreadyTakenMessage(User user) {
        return ResponseEntity.
                status(HttpStatus.CONFLICT).
                body(String.format("user name %s is already taken", user.getUserName()));
    }

    private ResponseEntity<String> createNewUser(SignUpDTO signUpDTO) {
        userRepo.save(createUserFromSignUpDTO(signUpDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private User createUserFromSignUpDTO(SignUpDTO signUpDTO) {
        return User.
                builder().
                userName(signUpDTO.getUserName()).
                password(signUpDTO.getPassword()).
                authority(authorityRepo.findByRole("ROLE_USER")).
                enabled(true).
                build();
    }
}
