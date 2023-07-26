package bi.seke.deliveryservice.configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfigurations {
    @Bean
    public NewTopic packageTopic(final Configurations configs) {
        return TopicBuilder.name(configs.getPackageTopicName())
                .partitions(configs.getNumberOfKafkaBrokers())
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, configs.getPackageTopicCleanPolicy())
                .build();
    }

    @Bean
    public NewTopic cancelTopic(final Configurations configs) {
        return TopicBuilder.name(configs.getCancelTopicName())
                .partitions(configs.getNumberOfKafkaBrokers())
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, configs.getCancelTopicCleanPolicy())
                .build();
    }
}
