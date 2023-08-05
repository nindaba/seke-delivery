package bi.seke.pricingservice.repositories;

import bi.seke.pricingservice.entities.DefinitePriceEntity;
import bi.seke.pricingservice.entities.PricePK;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Optional;

public interface DefinitePriceRepository extends CassandraRepository<DefinitePriceEntity, PricePK> {
    @AllowFiltering
    Optional<DefinitePriceEntity> findAllByPkPackageUid(String packageUid);
}
