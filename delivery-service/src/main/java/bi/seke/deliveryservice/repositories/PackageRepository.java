package bi.seke.deliveryservice.repositories;

import bi.seke.deliveryservice.entities.PackageEntity;
import bi.seke.deliveryservice.entities.PackagePK;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface PackageRepository extends CassandraRepository<PackageEntity, PackagePK> {
}
