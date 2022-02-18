package codeit.gatcha.api.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseFactory
{
    public static ResponseEntity<APIResponse> createResponse(String message, HttpStatus httpStatus)
    {
        return ResponseEntity
                .status(httpStatus)
                .body(new APIResponse(httpStatus.value(), message));
    }
}
