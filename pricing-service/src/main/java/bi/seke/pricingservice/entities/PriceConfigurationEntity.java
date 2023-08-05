package bi.seke.pricingservice.entities;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table
public class PriceConfigurationEntity implements Serializable {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 0)
    private String target;
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1)
    private boolean active;
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 2)
    private UUID uid = Uuids.timeBased();
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 3)
    private Double amount;
    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 4)
    private String name;
    private Double min;
    private Double max;
    private String unit;
    private String description;
    private LocalDateTime dateCreated;
    private LocalDateTime dateStopped;
}
