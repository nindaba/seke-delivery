package bi.seke.pricingservice.strategies.impl;

import bi.seke.pricingservice.entities.PriceConfigurationEntity;
import bi.seke.pricingservice.repositories.PriceConfigurationRepository;
import bi.seke.pricingservice.strategies.PriceCalculationStrategy;
import bi.seke.schema.deliveryservice.PackageDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.function.Consumer;

@Component
@AllArgsConstructor
@Log4j2
public class VolumePriceCalculationStrategy implements PriceCalculationStrategy {
    public static final String TARGET_VOLUME = "volume";

    protected final PriceConfigurationRepository repository;

    @Override
    public void calculate(final PackageDTO packag, final PriceDTO price) {
        Assert.notNull(packag, "Packag must not be null");
        Assert.notNull(price, "Packag must not be null");
        final Double volume = packag.getVolume();


        if (volume != null) {
            repository.findAllByTarget(TARGET_VOLUME).stream()
                    .filter(config -> config.getMin() <= volume && config.getMax() >= volume)
                    .findFirst()
                    .ifPresent(applyConfiguration(packag, price));
        }
    }

    protected Consumer<PriceConfigurationEntity> applyConfiguration(PackageDTO packag, PriceDTO price) {
        return config -> {
            log.info("Applying Price configuration {} to package {}", config.getName(), packag.getPackageUid());

            price.getTargetsAmounts()
                    .compute(TARGET_VOLUME, (key, value) -> {
                        log.info("Replacing the old {} price {} with {} for package {}",
                                TARGET_VOLUME, value, config.getAmount(), packag.getPackageUid());
                        return config.getAmount();
                    });
        };
    }
}
