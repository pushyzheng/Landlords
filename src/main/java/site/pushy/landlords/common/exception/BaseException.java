package site.pushy.landlords.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = 6353698331245900987L;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
}
