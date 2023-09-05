package bi.seke.schema.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class SourceDTO implements Serializable {
    private String uid;
    private String serviceName;
    private AddressDTO location;
    private int priority;
}
