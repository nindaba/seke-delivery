package bi.seke.paymentservice.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.UUID;

@Document
@Data
public class TaskDocument implements Serializable {
    @Id
    private UUID id;
    private String packageUid;
    private Integer retries;
    private boolean maxRetriesReached;
}
