package bi.seke.pricingservice.consumers;

import bi.seke.pricingservice.services.PriceConfigurationService;
import bi.seke.schema.deliveryservice.PackageDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import static bi.seke.pricingservice.configurations.Configurations.PACKAGE_TOPIC_NAME;


@Configuration
@AllArgsConstructor
@Log4j2
public class PackageTopicConsumer {
    protected PriceConfigurationService service;

    @KafkaListener(topics = PACKAGE_TOPIC_NAME)
    public void consumer(final PackageDTO packag) {
        log.info("Consumed Package {}", packag.getPackageUid());
        final PriceDTO price = service.calculatePackagePrice(packag);
        log.info("Calculated {}", price::getAmount);
    }
}
