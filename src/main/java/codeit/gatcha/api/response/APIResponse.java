package codeit.gatcha.api.response;

import lombok.*;

@AllArgsConstructor
@Getter @ToString
@NoArgsConstructor
public class APIResponse {
    private Object body;
    private int statusCode;
    private String message;

    public APIResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.body = null;
    }
}
