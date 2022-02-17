package codeit.gatcha;

import codeit.gatcha.api.client.DTO.APIResponse;
import codeit.gatcha.api.errorHandler.GlobalErrorHandling;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

@ExtendWith(MockitoExtension.class)
public class GlobalErrorHandlingTest {
    @InjectMocks
    private GlobalErrorHandling globalErrorHandling;

    @Test
    void givenInternalServerError_GetResponse(){
        ResponseEntity result = globalErrorHandling.internalServerErrorHandler(new Exception());
        assertEquals(INTERNAL_SERVER_ERROR, result.getStatusCode());

        APIResponse body = (APIResponse) result.getBody();
        assertEquals(INTERNAL_SERVER_ERROR.value(), body.getStatusCode());
        assertEquals("An error happened in the API, please report the incident", body.getMessage());
    }

    @Test
    void givenMethodNotFoundError_GetResponse(){
        ResponseEntity result = globalErrorHandling.methodNotAllowedErrorHandler(new HttpRequestMethodNotSupportedException("test"));
        assertEquals(result.getStatusCode(), METHOD_NOT_ALLOWED);
        APIResponse body = (APIResponse) result.getBody();
        assertEquals("An unsupported method call, please check the API docs", body.getMessage());
        assertEquals(METHOD_NOT_ALLOWED.value(), body.getStatusCode());
    }

}
