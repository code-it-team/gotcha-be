package codeit.gatcha.application.global.DTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SingleMessageResponse {
    private final String response;

    public String getResponse() {
        return this.response;
    }
}
