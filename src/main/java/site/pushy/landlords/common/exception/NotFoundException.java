package site.pushy.landlords.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Pushy
 * @since 2019/1/1 16:54
 */
public class NotFoundException extends BaseException {
    private static final long serialVersionUID = -4459741394856353269L;

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
