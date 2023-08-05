package bi.seke.schema.pricingservice;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class PriceDTO implements Serializable {
    private UUID uid;
    private String packageUid;
    private Double amount;
    private Map<String, Double> detailedAmount = new HashMap<>();
}
