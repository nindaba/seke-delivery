package bi.seke.deliveryservice.repositories;

import bi.seke.deliveryservice.entities.PackageEntity;
import bi.seke.deliveryservice.entities.PackagePK;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

public interface PackageRepository extends CassandraRepository<PackageEntity, PackagePK> {
    @AllowFiltering
    List<PackageEntity> findAllByPkCustomerUid(UUID customerUid);

    @AllowFiltering
    void deleteAllByPkCustomerUid(UUID customerUid);

}
