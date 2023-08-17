package bi.seke.pricingservice.services;

import bi.seke.pricingservice.configurations.Configurations;
import bi.seke.pricingservice.exceptions.PriceNotFoundException;
import bi.seke.schema.deliveryservice.PackageDTO;
import bi.seke.schema.pricingservice.PriceDTO;

public interface PriceService {
    /**
     * Creates PriceDTO and <br>
     * Runs all the {@link Configurations#priceCalculationStrategies} and combines their results to {@link PriceDTO#amount}
     *
     * @param packag package to be calculated for
     * @return price
     */
    PriceDTO calculatePackagePrice(PackageDTO packag);

    /**
     * Gets the price for the package
     *
     * @param packageUid represent package
     * @return price for the package with {@code packageUid}
     * @throws {@link PriceNotFoundException} if the packages price has not calculated yet or not found
     */
    PriceDTO getDefinitePrice(String packageUid) throws PriceNotFoundException;
}
