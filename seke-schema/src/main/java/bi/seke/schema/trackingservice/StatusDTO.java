package bi.seke.schema.trackingservice;

import bi.seke.schema.common.SourceDTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class StatusDTO implements Serializable {
    private String packageUid;
    private String message;
    private String datetime;
    private LocalDateTime dateTime;
    private String details;
    private SourceDTO source;
    private int statusNumber;
    private boolean done;
    private LocalDate expectedOn;
}
