package bi.seke.deliveryservice.exceptions;

public class MappingException extends RuntimeException {
    public MappingException(Exception e) {
        super(e);
    }
}
