package bi.seke.deliveryservice.entities;

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
    private UUID typeUid;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String value;
    @CassandraType(type = CassandraType.Name.BOOLEAN)
    private boolean active;
}
