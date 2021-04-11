package codeit.gatcha.user.controller;

import codeit.gatcha.user.DTO.SignUpDTO;
import codeit.gatcha.user.service.signUp.SingUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequiredArgsConstructor
public class SignUpController {
    private final SingUpService singUpService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDTO signUpDTO){
        return singUpService.signUpAndSendConfirmationEmail(signUpDTO);
    }

    @GetMapping(value="/confirm-account")
    public ResponseEntity<?> confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        return singUpService.confirmUserAccount(confirmationToken);
    }

}
