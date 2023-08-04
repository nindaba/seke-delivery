package bi.seke.schema.pricingservice;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class PriceDTO implements Serializable {
    private String uid;
    private String deliveryUid;
    private Double amount;
    private Map<String, Double> targetsAmounts = new HashMap<>();
}
