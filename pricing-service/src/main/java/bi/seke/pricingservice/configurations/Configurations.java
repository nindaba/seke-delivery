package bi.seke.pricingservice.configurations;

import bi.seke.pricingservice.strategies.PriceCalculationStrategy;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "config")
@Data
@Log4j2
public class Configurations {
    public static final String ROUTE_TOPIC_NAME = "${config.route-topic-name}";
    public static final String PACKAGE_TOPIC_NAME = "${config.package-topic-name}";
    public static final String COMMA_DELIMITER = ",";
    public static final String CACHE_KEY_FORMATTER = "%s::%s";
    public static final String PRICING_CONFIGS_CACHE = "PricingConfigs";
    public static final String PRICES_CACHE = "Prices";

    private String dateFormat = "dd-MM-YY";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
    private Integer numberOfKafkaBrokers = 1;

    //KAFKA
    private String pricingTopicName;
    private String packageTopicName;
    private String routeTopicName;
    private String pricingTopicCleanPolicy;

    //REDIS
    private String pricingConfigsCacheName;

    @Bean
    protected List<PriceCalculationStrategy> priceCalculationStrategies(
            final PriceCalculationStrategy weightPriceCalculationStrategy,
            final PriceCalculationStrategy volumePriceCalculationStrategy) {

        final List<PriceCalculationStrategy> strategies = new ArrayList<>();

        strategies.add(weightPriceCalculationStrategy);
        strategies.add(volumePriceCalculationStrategy);

        return strategies;
    }

    @Bean
    protected String pricingConfigsCache() {
        return "PricingConfigurations";
    }
}
