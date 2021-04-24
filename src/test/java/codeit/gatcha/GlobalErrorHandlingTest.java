package codeit.gatcha;

import codeit.gatcha.application.global.DTO.SingleMessageResponse;
import codeit.gatcha.application.global.GlobalErrorHandling;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GlobalErrorHandlingTest {
    @InjectMocks
    private GlobalErrorHandling globalErrorHandling;

    @Test
    void givenInternalServerError_GetResponse(){
        ResponseEntity result = globalErrorHandling.internalServerErrorHandler(new Exception());
        assertEquals(result.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals("An error happened in the API, please report the incident", ((SingleMessageResponse)result.getBody()).getResponse());
    }

    @Test
    void givenMethodNotFoundError_GetResponse(){
        ResponseEntity result = globalErrorHandling.methodNotAllowedErrorHandler(new HttpRequestMethodNotSupportedException("test"));
        assertEquals(result.getStatusCode(), HttpStatus.METHOD_NOT_ALLOWED);
        assertEquals("An unsupported method call, please check the API docs", ((SingleMessageResponse)result.getBody()).getResponse());
    }

}
