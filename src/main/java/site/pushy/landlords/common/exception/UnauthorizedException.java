package site.pushy.landlords.common.exception;

/**
 * @author Pushy
 * @since 2019/1/1 16:58
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
