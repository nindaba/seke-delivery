package bi.seke.paymentservice.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.UUID;

@Data
@Document
public class PriceDocument implements Serializable {
    @Id
    private UUID uid;
    private String packageUid;
    private Double amount;
}
