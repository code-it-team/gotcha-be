package codeit.gatcha.API.client.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor @Getter @ToString
public class APIResponse {
    private final Object body;
    private final int statusCode;
    private final String message;

    public APIResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.body = null;
    }
}
