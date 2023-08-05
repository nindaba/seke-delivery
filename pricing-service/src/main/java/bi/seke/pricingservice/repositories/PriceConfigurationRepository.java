package bi.seke.pricingservice.repositories;

import bi.seke.pricingservice.entities.PriceConfigurationEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static bi.seke.pricingservice.configurations.Configurations.PRICING_CONFIGS_CACHE;

public interface PriceConfigurationRepository extends CassandraRepository<PriceConfigurationEntity, UUID> {
    @AllowFiltering
    @Cacheable(key = "#target", value = PRICING_CONFIGS_CACHE)
    Collection<PriceConfigurationEntity> findAllByTarget(String target);

    @AllowFiltering
    @Cacheable(key = "#target+':active'", value = PRICING_CONFIGS_CACHE)
    Collection<PriceConfigurationEntity> findAllByActiveTrueAndTarget(String target);

    @AllowFiltering
    Optional<PriceConfigurationEntity> findByUid(UUID uid);

    @Override
    /** as we write few times than we read, we can evict the whole cache as it will happen few times */
    @CacheEvict(allEntries = true, value = PRICING_CONFIGS_CACHE)
    <S extends PriceConfigurationEntity> S save(S priceConfig);
}
