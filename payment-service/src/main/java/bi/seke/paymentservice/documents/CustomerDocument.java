package bi.seke.paymentservice.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class CustomerDocument {
    @Id
    private String id;
    private String customerUid;
    private Double balance;
}
