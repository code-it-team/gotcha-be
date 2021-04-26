package codeit.gatcha.API;


import codeit.gatcha.API.DTO.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@ControllerAdvice
public class GlobalErrorHandling {
    private final Logger log = LoggerFactory.getLogger(GlobalErrorHandling.class);

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> internalServerErrorHandler(Exception e){
        log.error("An internal server error happened",e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).
                body(new APIResponse(INTERNAL_SERVER_ERROR.value(), "An error happened in the API, please report the incident"));
    }

    @ResponseStatus(METHOD_NOT_ALLOWED)
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<APIResponse> methodNotAllowedErrorHandler(HttpRequestMethodNotSupportedException e){
        log.error(String.format("An unsupported method call %s", e.getMessage()));
        return ResponseEntity.status(METHOD_NOT_ALLOWED).
                body(new APIResponse( METHOD_NOT_ALLOWED.value(),"An unsupported method call, please check the API docs"));
    }
}