package bi.seke.paymentservice.services.impl;

import bi.seke.paymentservice.services.PaymentService;
import bi.seke.paymentservice.strategies.ConfirmationRetryStrategy;
import bi.seke.paymentservice.strategies.ConfirmationStrategy;
import bi.seke.schema.paymentservice.ConfirmationDTO;
import bi.seke.schema.paymentservice.ConfirmationStatus;
import bi.seke.schema.paymentservice.PaymentDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service("paymentService")
@Log4j2
@AllArgsConstructor
public class DefaultPaymentService implements PaymentService {
    protected final ConfirmationStrategy strategy;
    protected final ConfirmationRetryStrategy retryStrategy;

    @Override
    public void confirmPayment(PaymentDTO payment) {
        log.info("Creating And Sending Confirmation for package {}", payment::getPackageUid);
        final ConfirmationDTO confirmation = strategy.createAndSendConfirmation(payment);

        if (confirmation.getStatus() != ConfirmationStatus.ACCEPTED) {
            log.info("Payment for {} will be retried as, {}",
                    payment.getPackageUid(), confirmation.getMessage());

            retryStrategy.startConfirmationRetryTasks(payment);
        }
    }

}
