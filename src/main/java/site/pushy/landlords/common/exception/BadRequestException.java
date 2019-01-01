package site.pushy.landlords.common.exception;

/**
 * @author Pushy
 * @since 2019/1/1 16:50
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
