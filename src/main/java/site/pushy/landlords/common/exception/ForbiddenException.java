package site.pushy.landlords.common.exception;

/**
 * @author Pushy
 * @since 2019/1/1 16:59
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
