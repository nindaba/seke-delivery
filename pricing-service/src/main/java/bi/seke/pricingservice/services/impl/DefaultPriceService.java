package bi.seke.pricingservice.services.impl;

import bi.seke.pricingservice.entities.DefinitePriceEntity;
import bi.seke.pricingservice.entities.PricePK;
import bi.seke.pricingservice.exceptions.PriceNotFoundException;
import bi.seke.pricingservice.repositories.DefinitePriceRepository;
import bi.seke.pricingservice.services.PriceConfigurationService;
import bi.seke.pricingservice.services.PriceService;
import bi.seke.pricingservice.strategies.PriceCalculationStrategy;
import bi.seke.schema.deliveryservice.PackageDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import java.util.List;

import static bi.seke.pricingservice.configurations.Configurations.PRICES_CACHE;

@Component
@AllArgsConstructor
@Log4j2
public class DefaultPriceService implements PriceService {
    protected final DefinitePriceRepository repository;
    protected final PriceConfigurationService service;
    protected final List<PriceCalculationStrategy> priceCalculationStrategies;


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

        repository.save(convertPrice(price));
        return price;
    }

    protected DefinitePriceEntity convertPrice(final PriceDTO price) {
        final DefinitePriceEntity target = new DefinitePriceEntity();
        final PricePK pk = new PricePK();

        pk.setPackageUid(price.getPackageUid());
        pk.setUid(price.getUid());
        target.setPk(pk);
        target.setAmount(price.getAmount());

        return target;
    }

    @Override
    public PriceDTO getDefinitePrice(String packageUid) throws PriceNotFoundException {
        return repository.findAllByPkPackageUid(packageUid)
                .map(price -> {
                    final PriceDTO priceDTO = new PriceDTO();

                    priceDTO.setAmount(price.getAmount());
                    priceDTO.setUid(price.getPk().getUid());
                    priceDTO.setPackageUid(price.getPk().getPackageUid());

                    return priceDTO;
                })
                .orElseThrow(() -> new PriceNotFoundException("The packages %s price has not calculated yet or not found".formatted(packageUid)));
    }
}
