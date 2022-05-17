package site.pushy.landlords.common.exception;

import org.springframework.http.HttpStatus;

/**
 * @author Pushy
 * @since 2019/1/1 16:59
 */
public class ForbiddenException extends BaseException {
    private static final long serialVersionUID = -1346248998215060656L;

    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
