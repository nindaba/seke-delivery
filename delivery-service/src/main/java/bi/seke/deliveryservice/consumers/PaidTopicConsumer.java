package bi.seke.deliveryservice.consumers;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

import static bi.seke.deliveryservice.configurations.Configurations.PAID_TOPIC_NAME;

@Configuration
public class PaidTopicConsumer {

    @KafkaListener(topics = PAID_TOPIC_NAME)
    public void consumer(final String routeUid) {
    }
}
