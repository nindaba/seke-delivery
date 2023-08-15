package bi.seke.paymentservice.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
@Data
public class CustomerDocument {
    @Id
    private UUID uuid;
    private String customerUid;
    private Double balance;
}
