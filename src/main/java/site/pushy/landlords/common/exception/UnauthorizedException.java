package site.pushy.landlords.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Pushy
 * @since 2019/1/1 16:58
 */
public class UnauthorizedException extends BaseException {
    private static final long serialVersionUID = -6741834004797894557L;

    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
