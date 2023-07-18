package bi.seke.deliveryservice.entities;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

@Table
@Data
public class DeliveryTypeEntity implements Serializable {
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    private UUID typeUid = Uuids.timeBased();
    ;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String value;
    @CassandraType(type = CassandraType.Name.BOOLEAN)
    private boolean active;
}
