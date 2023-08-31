package bi.seke.paymentservice.strategies.impl;

import bi.seke.paymentservice.configurations.Configurations;
import bi.seke.paymentservice.exceptions.WritingException;
import bi.seke.paymentservice.services.PriceService;
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
    protected final KafkaTemplate<String, Serializable> template;
    protected final Configurations configurations;

    @Override
    public ConfirmationDTO createAndSendConfirmation(PaymentDTO payment) {
        final ConfirmationStatus status = priceService.getPrice(payment.getPackageUid())
                .map(getPaymentStatus(payment))
                .orElse(ConfirmationStatus.REJECTED);
        final ConfirmationDTO confirmation = createConfirmation(payment, status);

        log.debug("Created Confirmation for {} with status {}", confirmation.getPackageUid(), confirmation.getStatus());
        priceService.reset(payment.getPackageUid());

        if (configurations.getAcceptedConfirmationStatuses().contains(status)) {
            priceService.markPaid(payment.getPackageUid());
            template.send(configurations.getPaidTopicName(), payment.getPackageUid(), confirmation)
                    .whenComplete(this::logKafkaResults);
        }
        return confirmation;
    }

    protected ConfirmationDTO createConfirmation(final PaymentDTO payment, final ConfirmationStatus status) {
        final ConfirmationDTO confirmation = new ConfirmationDTO();

        confirmation.setPackageUid(payment.getPackageUid());
        confirmation.setStatus(status);
        setMessage(confirmation, payment.getAmount());
        return confirmation;
    }

    protected Function<PriceDTO, ConfirmationStatus> getPaymentStatus(PaymentDTO payment) {
        return priceDto -> configurations.getPaymentConfirmationStatuses()
                .getOrDefault(Double.compare(payment.getAmount(), priceDto.getAmount()),
                        ConfirmationStatus.REJECTED);
    }

    private void setMessage(final ConfirmationDTO confirmation, final Double paid) {
        if (confirmation.getStatus() != ConfirmationStatus.ACCEPTED) {
            priceService.getPrice(confirmation.getPackageUid())
                    .ifPresent(price -> confirmation
                            .setMessage("The paid amount %s does not match the price %s".formatted(paid, price.getAmount()))
                    );
        } else {
            confirmation.setMessage("Confirmed ✅");
        }
    }


    protected void logKafkaResults(final SendResult<String, Serializable> results, final Throwable throwable) {
        if (throwable != null) {
            log.error("Could not send the Package to kafka topic", throwable);
            throw new WritingException(throwable);
        }

        log.debug("Sent Package {} to {}", results.getProducerRecord().key(), results.getRecordMetadata().topic());
    }
}
