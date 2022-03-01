package codeit.gatcha.api.controller;

import codeit.gatcha.api.response.APIResponse;
import codeit.gatcha.api.security.service.API_SignUpService;
import codeit.gatcha.domain.user.DTO.SignUpDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SignupController {
    private final API_SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<APIResponse> signUp(@RequestBody @Validated SignUpDTO signUpDTO){
        return signUpService.signUpAndSendConfirmationEmail(signUpDTO);
    }

    @GetMapping(value="/confirm-account")
    public ResponseEntity<APIResponse> confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        return signUpService.confirmUserAccount(confirmationToken);
    }

}