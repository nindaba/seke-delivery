package bi.seke.deliveryservice.consumers;

import bi.seke.schema.deliveryservice.RoutesDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import static bi.seke.deliveryservice.configurations.Configurations.ROUTE_TOPIC_NAME;

@Configuration
@Log4j2
public class RoutingTopicConsumer {
    @KafkaListener(topics = ROUTE_TOPIC_NAME)
    public void consumer(final RoutesDTO routes) {

        log.info("Consumed route {}", routes.getPackageUid());
    }
}
