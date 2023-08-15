package bi.seke.paymentservice.strategies.impl;

import bi.seke.schema.paymentservice.ConfirmationDTO;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

public class KafkaTemplateTest extends KafkaTemplate<String, ConfirmationDTO> {
    public KafkaTemplateTest() {
        super(new DefaultKafkaProducerFactory<>(Map.of()));
    }

    public KafkaTemplateTest(ProducerFactory<String, ConfirmationDTO> producerFactory) {
        super(producerFactory);
    }

    public KafkaTemplateTest(ProducerFactory<String, ConfirmationDTO> producerFactory, Map<String, Object> configOverrides) {
        super(producerFactory, configOverrides);
    }

    public KafkaTemplateTest(ProducerFactory<String, ConfirmationDTO> producerFactory, boolean autoFlush) {
        super(producerFactory, autoFlush);
    }

    public KafkaTemplateTest(ProducerFactory<String, ConfirmationDTO> producerFactory, boolean autoFlush, Map<String, Object> configOverrides) {
        super(producerFactory, autoFlush, configOverrides);
    }
}
