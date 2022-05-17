package site.pushy.landlords.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Pushy
 * @since 2019/1/1 16:50
 */
public class BadRequestException extends BaseException {
    private static final long serialVersionUID = -637828891980306935L;

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
