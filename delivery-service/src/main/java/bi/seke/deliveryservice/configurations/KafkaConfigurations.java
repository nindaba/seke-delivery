package bi.seke.deliveryservice.configurations;

import bi.seke.data.dtos.PackageDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

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

    /**
     * toMoveToRelatedServices: {
     */
    @Bean
    public NewTopic paidTopic(final Configurations configs) {
        return TopicBuilder.name(configs.getPaidTopicName())
                .partitions(configs.getNumberOfKafkaBrokers())
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, configs.getCancelTopicCleanPolicy())
                .build();
    }

    @Bean
    public NewTopic routeTopic(final Configurations configs) {
        return TopicBuilder.name(configs.getRouteTopicName())
                .partitions(configs.getNumberOfKafkaBrokers())
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, configs.getCancelTopicCleanPolicy())
                .build();
    }

    @Bean
    public KafkaTemplate<String, PackageDTO> packageTemplate(final KafkaProperties properties) {
        final DefaultKafkaProducerFactory<String, PackageDTO> factory = new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
        return new KafkaTemplate<>(factory);
    }
}
