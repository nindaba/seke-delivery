package bi.seke.paymentservice.strategies.impl;

import bi.seke.paymentservice.configurations.Configurations;
import bi.seke.paymentservice.exceptions.WritingException;
import bi.seke.paymentservice.services.PriceService;
import bi.seke.paymentservice.strategies.ConfirmationRetryStrategy;
import bi.seke.paymentservice.strategies.ConfirmationStrategy;
import bi.seke.schema.paymentservice.ConfirmationDTO;
import bi.seke.schema.paymentservice.ConfirmationStatus;
import bi.seke.schema.paymentservice.PaymentDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.function.Function;

@Service
@AllArgsConstructor
@Log4j2
public class DefaultConfirmationStrategy implements ConfirmationStrategy {
    protected final PriceService priceService;
    protected final KafkaTemplate<String, ConfirmationDTO> template;
    protected final Configurations configurations;
    protected final ConfirmationRetryStrategy retryStrategy;

    @Override
    public void createAndSendConfirmation(PaymentDTO payment) {

        final ConfirmationDTO confirmation = new ConfirmationDTO();
        final ConfirmationStatus status = priceService.getPrice(payment.getPackageUid())
                .map(getPaymentStatus(payment))
                .orElse(ConfirmationStatus.REJECTED);
        confirmation.setStatus(status);

        log.info("Created Confirmation for {} with status {}", confirmation.getPackageUid(), confirmation.getStatus());

        if (configurations.getAcceptedConfirmationStatuses().contains(status)) {

            priceService.markPaid(payment.getPackageUid());
            setMessage(confirmation);
            template.send(configurations.getPaidTopicName(), payment.getPackageUid(), confirmation)
                    .whenComplete(this::logKafkaResults);
        }

        if (status != ConfirmationStatus.ACCEPTED) {
            log.info("""
                            Payment for {} has status {} and it will be retried as the charged amount does not match the price,\s
                            such that the overcharged amount can be returned in case the price does not change,\s
                            or return the payment in case does not match the price""",
                    payment.getPackageUid(), status);
            retryStrategy.createRetryConfirmation(payment);
        }
    }

    protected Function<PriceDTO, ConfirmationStatus> getPaymentStatus(PaymentDTO payment) {
        return priceDto -> configurations.getPaymentConfirmationStatuses()
                .getOrDefault(Double.compare(payment.getAmount(), priceDto.getAmount()),
                        ConfirmationStatus.REJECTED);
    }

    private void setMessage(final ConfirmationDTO confirmation) {
        if (confirmation.getStatus() != ConfirmationStatus.ACCEPTED) {
            confirmation.setMessage("This confirmation will be repeated since the paid amount does not match the price ⚠️");
        } else {
            confirmation.setMessage("Confirmed ✅");
        }
    }


    protected void logKafkaResults(final SendResult<String, ? extends Serializable> results, final Throwable throwable) {
        if (throwable != null) {
            log.error("Could not send the Package to kafka topic", throwable);
            throw new WritingException(throwable);
        }

        log.debug("Sent Package {} to {}", results.getProducerRecord().key(), results.getRecordMetadata().topic());
    }
}
