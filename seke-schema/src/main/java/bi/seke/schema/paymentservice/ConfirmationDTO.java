package bi.seke.schema.paymentservice;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfirmationDTO implements Serializable {
    private String packageUid;
    private ConfirmationStatus status;
    private String message;
}
