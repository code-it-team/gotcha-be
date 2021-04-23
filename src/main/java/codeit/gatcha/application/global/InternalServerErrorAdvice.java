package codeit.gatcha.application.global;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class InternalServerErrorAdvice {
    private final Logger log = LoggerFactory.getLogger(InternalServerErrorAdvice.class);

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> internalServerErrorHandler(RuntimeException e){
        log.error("An internal server error happened",e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("An error happened in the API, please report the incident");
    }
}