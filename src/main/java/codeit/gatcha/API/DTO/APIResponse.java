package codeit.gatcha.API.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class APIResponse {
    private final Object response;
    private final int statusCode;
}
