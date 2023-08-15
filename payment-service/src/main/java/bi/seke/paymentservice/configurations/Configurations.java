package bi.seke.paymentservice.configurations;

import bi.seke.schema.paymentservice.ConfirmationStatus;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "config")
@Data
@Log4j2
public class Configurations {
    public static final String PAID_TOPIC_NAME = "${config.paid-topic-name}";
    public static final String CANCEL_TOPIC_NAME = "${config.cancel-topic-name}";

    private String dateFormat = "dd-MM-YY";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);

    private Integer priceMismatchRetries = 3;
    /**
     * duration in seconds to retry the confirmation
     */
    private Integer priceMismatchRetryDelay = 3;
    private boolean priceMismatchReturn;
    private Map<Integer, ConfirmationStatus> paymentConfirmationStatuses;
    private List<ConfirmationStatus> acceptedConfirmationStatuses;

    //KAFKA
    private Integer numberOfKafkaBrokers = 1;
    private String paidTopicName;
    private String pricingTopicName;
    private String cancelTopicName;
    private String paidTopicCleanPolicy;
}
