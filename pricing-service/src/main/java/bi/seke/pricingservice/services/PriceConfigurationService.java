package bi.seke.pricingservice.services;

import bi.seke.pricingservice.entities.PriceConfigurationEntity;

import java.util.Collection;
import java.util.Optional;

public interface PriceConfigurationService {

    /**
     * Gets all the price configurations
     *
     * @return collection of price configurations
     */
    Collection<PriceConfigurationEntity> getAllPriceConfigurations();

    /**
     * Gets all the price configurations with {@code target}
     *
     * @param target the field name of the package, where configuration will be applied
     * @return collection of price configurations
     */
    Collection<PriceConfigurationEntity> getAllPriceConfigurations(String target);

    /**
     * Gets a price configuration by uid
     *
     * @param uid of the price configuration to get
     * @return optional of price configuration
     */
    Optional<PriceConfigurationEntity> getPriceConfiguration(String uid);


    /**
     * Deletes price configurations with uid in {@code uids}
     *
     * @param uids collection of uids of price configurations to be deleted
     */
    void deletePriceConfigurations(Collection<String> uids);

    /**
     * Saves the price configuration and returns the new or updated price configuration
     *
     * @param priceConfiguration
     * @return price configuration
     */
    PriceConfigurationEntity savePriceConfiguration(PriceConfigurationEntity priceConfiguration);

    /**
     * Saves all the price configurations
     *
     * @param configs a collection of price configurations
     */
    void savePriceConfiguration(Collection<PriceConfigurationEntity> configs);
}
