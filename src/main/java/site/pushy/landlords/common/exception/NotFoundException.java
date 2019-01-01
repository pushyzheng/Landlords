package site.pushy.landlords.common.exception;

/**
 * @author Pushy
 * @since 2019/1/1 16:54
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
