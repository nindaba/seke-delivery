package bi.seke.deliveryservice.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
@Data
public class PackageEntity {
    @PrimaryKey
    private PackagePK pk;
    @CassandraType(type = CassandraType.Name.TEXT)
    private UUID routeID;
    @CassandraType(type = CassandraType.Name.DOUBLE)
    private double weight;
    @CassandraType(type = CassandraType.Name.DOUBLE)
    private double volume;
    @CassandraType(type = CassandraType.Name.BOOLEAN)
    private boolean fragile;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String email;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String phone;
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID deliveryType;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String description;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String createdBy;
}
