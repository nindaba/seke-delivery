package bi.seke.deliveryservice.exceptions;

public class WritingException extends RuntimeException {
    public WritingException(final Throwable throwable) {
        super(throwable);
    }

    public WritingException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
