package codeit.gatcha.api.controller;

import codeit.gatcha.api.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/admin/hello")
    public ResponseEntity<APIResponse> helloAdmin(){
        return ResponseEntity.ok().
                body(new APIResponse(HttpStatus.OK.value(), "Hey man!"));
    }
}
