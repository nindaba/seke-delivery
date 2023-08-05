package bi.seke.pricingservice.services.impl;

import bi.seke.pricingservice.entities.PriceConfigurationEntity;
import bi.seke.pricingservice.repositories.PriceConfigurationRepository;
import bi.seke.pricingservice.services.PriceConfigurationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DefaultPriceConfigurationService implements PriceConfigurationService {
    protected final PriceConfigurationRepository repository;

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

}
