package bi.seke.deliveryservice.repositories;

import bi.seke.deliveryservice.entities.DeliveryTypeEntity;
import bi.seke.deliveryservice.entities.PackagePK;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface DeliveryTypeRepository extends CassandraRepository<DeliveryTypeEntity, PackagePK> {
}
