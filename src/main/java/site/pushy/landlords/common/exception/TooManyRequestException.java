package site.pushy.landlords.common.exception;

import org.springframework.http.HttpStatus;

public class TooManyRequestException extends BaseException {
    private static final long serialVersionUID = 1345905428835996667L;

    public TooManyRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.TOO_MANY_REQUESTS;
    }
}
