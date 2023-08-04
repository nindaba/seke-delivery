package bi.seke.pricingservice.configurations;

import bi.seke.pricingservice.entities.PriceConfigurationEntity;
import bi.seke.pricingservice.services.PriceConfigurationService;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "prices")
@Data
@Log4j2
public class PriceConfigurationProperties {
    private List<PriceConfigurationEntity> configs;
    @NestedConfigurationProperty
    private PriceConfigurationEntity sampleConfig;
    private boolean saveProperties;


    @Bean
    public CommandLineRunner runner(final PriceConfigurationService service) {
        return args -> {
            log.info("Saving the price configurations from application properties ....");
            service.savePriceConfiguration(sampleConfig);
            service.savePriceConfiguration(configs);
            log.info("Done Saving the price configurations from application properties ✅");
        };
    }
}
