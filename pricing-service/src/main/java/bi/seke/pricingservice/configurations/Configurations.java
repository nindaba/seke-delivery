package bi.seke.pricingservice.configurations;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
@ConfigurationProperties(prefix = "config")
@Data
@Log4j2
public class Configurations {
    public static final String ROUTE_TOPIC_NAME = "${config.route-topic-name}";
    public static final String PACKAGE_TOPIC_NAME = "${config.package-topic-name}";
    public static final String COMMA_DELIMITER = ",";
    public static final String CACHE_KEY_FORMATTER = "%s::%s";


    private String dateFormat = "dd-MM-YY";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
    private Integer numberOfKafkaBrokers = 1;

    //KAFKA
    private String pricingTopicName;
    private String packageTopicName;
    private String routeTopicName;
    private String pricingTopicCleanPolicy;

    //REDIS
    private String priceCacheName;
    private String deliveryTypesCacheName;
}
