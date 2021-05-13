package codeit.gatcha.API.controller;

import codeit.gatcha.API.service.security.API_SignUpService;
import codeit.gatcha.API.DTO.APIResponse;
import codeit.gatcha.domain.user.DTO.SignUpDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequiredArgsConstructor
public class SignUpController {
    private final API_SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<APIResponse> signUp(@RequestBody SignUpDTO signUpDTO){
        return signUpService.signUpAndSendConfirmationEmail(signUpDTO);
    }

    @GetMapping(value="/confirm-account")
    public ResponseEntity<APIResponse> confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        return signUpService.confirmUserAccount(confirmationToken);
    }

}
