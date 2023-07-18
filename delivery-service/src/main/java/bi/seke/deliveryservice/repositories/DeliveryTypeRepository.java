package bi.seke.deliveryservice.repositories;

import bi.seke.deliveryservice.entities.DeliveryTypeEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface DeliveryTypeRepository extends CassandraRepository<DeliveryTypeEntity, UUID> {
}
