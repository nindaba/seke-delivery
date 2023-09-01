package bi.seke.schema.trackingservice;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TrackerDTO {
    private String trackingNumber;
    private String packageUid;
    private List<StatusDTO> statuses;
    private List<String> emails;
    private List<String> phones;
    private LocalDate estimatedDelivery;
}
