package bi.seke.pricingservice.configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.Serializable;

@Configuration
public class KafkaConfigurations {
    @Bean
    protected NewTopic priceTopic(final Configurations configs) {
        return TopicBuilder.name(configs.getPricingTopicName())
                .partitions(configs.getNumberOfKafkaBrokers())
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, configs.getPricingTopicCleanPolicy())
                .build();
    }

    @Bean
    public KafkaTemplate<String, Serializable> packageTemplate(final KafkaProperties properties) {
        final DefaultKafkaProducerFactory<String, Serializable> factory = new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
        return new KafkaTemplate<>(factory);
    }
}
