package bi.seke.trackingservice.configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfigurations {
    @Bean
    protected NewTopic trackingTopic(final Configurations configs) {
        return TopicBuilder.name(configs.getTrackingTopicName())
                .partitions(configs.getNumberOfKafkaBrokers())
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, configs.getTrackingTopicCleanPolicy())
                .build();
    }
}
