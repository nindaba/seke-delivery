package bi.seke.pricingservice.consumers;

import bi.seke.pricingservice.configurations.Configurations;
import bi.seke.pricingservice.exceptions.WritingException;
import bi.seke.pricingservice.services.PriceService;
import bi.seke.schema.deliveryservice.PackageDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.io.Serializable;

import static bi.seke.pricingservice.configurations.Configurations.PACKAGE_TOPIC_NAME;


@Configuration
@AllArgsConstructor
@Log4j2
public class PackageTopicConsumer {
    protected final PriceService service;
    protected final Configurations configurations;
    protected final KafkaTemplate<String, Serializable> packageTemplate;

    @KafkaListener(topics = PACKAGE_TOPIC_NAME)
    public void consumer(final PackageDTO packag) {
        log.debug("Consumed Package {}", packag.getPackageUid());
        final PriceDTO price = service.calculatePackagePrice(packag);
        packageTemplate.send(configurations.getPricingTopicName(), price.getPackageUid(), price)
                .whenComplete(this::logKafkaResults);
        log.debug("Calculated {}", price::getAmount);
    }

    protected void logKafkaResults(final SendResult<String, ? extends Serializable> results, final Throwable throwable) {
        final RecordMetadata metadata = results.getRecordMetadata();

        if (throwable != null) {
            log.error("Could not send the Package to kafka topic", throwable);
            throw new WritingException(throwable);
        }

        log.debug("Sent Price for {} to {} partition {}", results.getProducerRecord().key(), metadata.topic(), metadata.partition());
    }
}
