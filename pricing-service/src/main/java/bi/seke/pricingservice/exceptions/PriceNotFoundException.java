package bi.seke.pricingservice.exceptions;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(final String message) {
        super(message);
    }

    public PriceNotFoundException(final Throwable throwable) {
        super(throwable);
    }

    public PriceNotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
