package bi.seke.deliveryservice.dtos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.UUID;

@Data
@RedisHash()
public class PackageDTO implements Serializable {
    @Id
    @Indexed
    private String packageUid;
    @Indexed
    private UUID routeID;
    private double weight;
    private double volume;
    private boolean fragile;
    private String email;
    private String phone;
    private UUID deliveryType;
    private String description;
    private String createdB;
}
