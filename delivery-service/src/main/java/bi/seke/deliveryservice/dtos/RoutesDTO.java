package bi.seke.deliveryservice.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoutesDTO implements Serializable {
    private String packageUid;
    private List<RouteEntryDTO> entries;
}
