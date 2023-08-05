package bi.seke.pricingservice.services.impl;

import bi.seke.pricingservice.entities.PriceConfigurationEntity;
import bi.seke.pricingservice.repositories.PriceConfigurationRepository;
import bi.seke.pricingservice.services.PriceConfigurationService;
import bi.seke.pricingservice.strategies.PriceCalculationStrategy;
import bi.seke.schema.deliveryservice.PackageDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static bi.seke.pricingservice.configurations.Configurations.PRICES_CACHE;

@Service
@AllArgsConstructor
public class DefaultPriceConfigurationService implements PriceConfigurationService {
    protected final PriceConfigurationRepository repository;
    protected final List<PriceCalculationStrategy> priceCalculationStrategies;

    @Override
    public Collection<PriceConfigurationEntity> getAllPriceConfigurations() {
        return repository.findAll();
    }

    @Override
    public Collection<PriceConfigurationEntity> getAllPriceConfigurations(final String target) {
        return repository.findAllByTarget(target);
    }

    @Override
    public Optional<PriceConfigurationEntity> getPriceConfiguration(final String uid) {
        return repository.findByUid(UUID.fromString(uid));
    }

    @Override
    public void deletePriceConfigurations(final Collection<String> uids) {
        if (CollectionUtils.isEmpty(uids)) {
            repository.deleteAll();
        } else {
            repository.deleteAllById(uids.stream().map(UUID::fromString).toList());
        }
    }

    @Override
    public PriceConfigurationEntity savePriceConfiguration(PriceConfigurationEntity priceConfiguration) {
        return repository.save(priceConfiguration);
    }

    @Override
    public void savePriceConfiguration(Collection<PriceConfigurationEntity> configs) {
        repository.saveAll(configs);
    }

    @Override
    @CachePut(key = "#packag.packageUid", value = PRICES_CACHE)
    public PriceDTO calculatePackagePrice(PackageDTO packag) {
        final PriceDTO price = new PriceDTO();
        price.setPackageUid(packag.getPackageUid());
        price.setUid(Uuids.timeBased());

        priceCalculationStrategies.forEach(strategy -> strategy.calculate(packag, price));

        price.getDetailedAmount().values()
                .stream().reduce(Double::sum)
                .ifPresent(price::setAmount);

        return price;
    }
}
