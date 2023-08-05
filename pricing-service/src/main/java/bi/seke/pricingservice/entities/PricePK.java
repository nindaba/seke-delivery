package bi.seke.pricingservice.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.UUID;

@Data
@PrimaryKeyClass
public class PricePK implements Serializable {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID uid;
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private String packageUid;
}
