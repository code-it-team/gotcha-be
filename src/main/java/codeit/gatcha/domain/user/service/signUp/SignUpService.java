package codeit.gatcha.domain.user.service.signUp;

import codeit.gatcha.api.security.repo.ConfirmationTokenRepo;
import codeit.gatcha.domain.user.DTO.SignUpDTO;
import codeit.gatcha.domain.user.entity.GatchaUser;
import codeit.gatcha.domain.user.repo.IAuthorityRepo;
import codeit.gatcha.domain.user.repo.IUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class SignUpService {
    private final IUserRepo IUserRepo;
    private final IAuthorityRepo authorityRepo;
    private final ConfirmationTokenRepo confirmationTokenRepo;

    public GatchaUser createNewUser(SignUpDTO signUpDTO) {
        GatchaUser newUser = createUserFromSignUpDTO(signUpDTO);
        return IUserRepo.save(newUser);
    }

    private GatchaUser createUserFromSignUpDTO(SignUpDTO signUpDTO) {
        return GatchaUser.
                builder().
                email(signUpDTO.getEmail()).
                password(signUpDTO.getPassword()).
                authority(authorityRepo.findByRole("ROLE_USER").get()).
                enabled(false).
                build();
    }

    public void activateUserAccount(String confirmationTokenString) {
        confirmationTokenRepo.
                findByConfirmationToken(confirmationTokenString).
                ifPresent(ct -> enableUserAccount(ct.getUser()));
    }

    private void enableUserAccount(GatchaUser user) {
        user.setEnabled(true);
        IUserRepo.save(user);
    }
}
