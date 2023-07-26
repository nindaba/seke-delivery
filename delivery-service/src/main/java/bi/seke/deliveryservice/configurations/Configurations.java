package bi.seke.deliveryservice.configurations;

import bi.seke.deliveryservice.dtos.PackageDTO;
import bi.seke.deliveryservice.mappers.DTOMapper;
import bi.seke.deliveryservice.mappers.impl.PackageEntryToDTOMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "config")
@Data
@Log4j2
public class Configurations {
    public static final String DELIMITER = ",";
    private String dateFormat = "dd-MM-YY";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
    private Integer numberOfKafkaBrokers = 1;
    private String packageTopicName = "PackageTopic";
    private String packageTopicCleanPolicy = "%s,%s".formatted(TopicConfig.CLEANUP_POLICY_COMPACT, TopicConfig.CLEANUP_POLICY_DELETE);
    private String cancelTopicName = "CancelTopic";
    private String cancelTopicCleanPolicy = "%s,%s".formatted(TopicConfig.CLEANUP_POLICY_COMPACT, TopicConfig.CLEANUP_POLICY_DELETE);

    @Bean
    public Map<Class, DTOMapper> dtoMappers(final PackageEntryToDTOMapper packageMapper) {
        final Map<Class, DTOMapper> mappers = Map.of(
                PackageDTO.class, packageMapper
        );
        return mappers;
    }

    @Bean
    public KeyGenerator packageKeyGenerator() {
        return (target, method, params) -> params.length == 0 ? method.getName() : Arrays.stream(params)
                .map(param -> param instanceof PackageDTO packag ? packag.getPackageUid() : toString(params))
                .collect(Collectors.joining(DELIMITER));
    }

    private String toString(final Object[] params) {
        return Arrays.stream(params)
                .map(Object::toString)
                .collect(Collectors.joining(DELIMITER));
    }
}
