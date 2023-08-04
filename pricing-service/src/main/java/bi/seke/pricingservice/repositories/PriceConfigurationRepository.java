package bi.seke.pricingservice.repositories;

import bi.seke.pricingservice.entities.PriceConfigurationEntity;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PriceConfigurationRepository extends CassandraRepository<PriceConfigurationEntity, UUID> {

    @AllowFiltering
    Collection<PriceConfigurationEntity> findAllByTarget(String target);

    @AllowFiltering
    Collection<PriceConfigurationEntity> findAllByActiveTrueAndTarget(String target);

    @AllowFiltering
    Optional<PriceConfigurationEntity> findByUid(UUID uid);
}
