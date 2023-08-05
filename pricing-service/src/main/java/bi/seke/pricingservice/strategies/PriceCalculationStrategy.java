package bi.seke.pricingservice.strategies;

import bi.seke.schema.deliveryservice.PackageDTO;
import bi.seke.schema.pricingservice.PriceDTO;

public interface PriceCalculationStrategy {

    /**
     * Calculates the prices according to the price configuration, where each target value will be loaded from the package <br>
     * and multiplied with the configuration amount, add each target and result to {@link PriceDTO#detailedAmount} <br>
     * all the targets results will be added together to create a package amount
     *
     * @param packag the package to be calculated for
     * @param price  current price of the package
     */
    void calculate(PackageDTO packag, PriceDTO price);
}
