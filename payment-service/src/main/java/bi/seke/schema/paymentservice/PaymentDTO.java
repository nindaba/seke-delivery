package bi.seke.schema.paymentservice;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentDTO implements Serializable {
    private String packageUid;
    private String transactionId;
    private Double amount;
}
