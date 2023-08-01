package bi.seke.deliveryservice.configurations;

import bi.seke.data.dtos.PackageDTO;
import bi.seke.deliveryservice.mappers.DTOMapper;
import bi.seke.deliveryservice.mappers.impl.PackageEntryToDTOMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "config")
@Data
@Log4j2
public class Configurations {
    public static final String PAID_TOPIC_NAME = "${config.paid-topic-name}";
    public static final String ROUTE_TOPIC_NAME = "${config.route-topic-name}";
    public static final String PACKAGE_TOPIC_NAME = "${config.package-topic-name}";
    public static final String CANCEL_TOPIC_NAME = "${config.cancel-topic-name}";
    public static final String COMMA_DELIMITER = ",";
    public static final String CACHE_KEY_FORMATTER = "%s::%s";


    private String dateFormat = "dd-MM-YY";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
    private Integer numberOfKafkaBrokers = 1;

    //KAFKA
    private String packageTopicName;
    private String paidTopicName;
    private String routeTopicName;
    private String cancelTopicName;
    private String packageTopicCleanPolicy;
    private String cancelTopicCleanPolicy;

    //REDIS
    private String packageCacheName;
    private String deliveryTypesCacheName;

    //PACKAGE
    private double packageMinWeight;
    private double packageMaxWeight;


    @Bean
    public Map<Class, DTOMapper> dtoMappers(final PackageEntryToDTOMapper packageMapper) {
        final Map<Class, DTOMapper> mappers = Map.of(
                PackageDTO.class, packageMapper
        );
        return mappers;
    }

    @Bean
    public Collection<Validator> packageValidators(final Validator packageValidator) {
        return List.of(packageValidator);
    }
}
