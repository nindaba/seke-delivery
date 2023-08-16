package bi.seke.paymentservice.strategies.impl;

import bi.seke.paymentservice.configurations.Configurations;
import bi.seke.paymentservice.documents.CustomerDocument;
import bi.seke.paymentservice.documents.TaskDocument;
import bi.seke.paymentservice.repositories.CustomerRepository;
import bi.seke.paymentservice.repositories.TaskRepository;
import bi.seke.paymentservice.services.PackageUidService;
import bi.seke.paymentservice.services.PriceService;
import bi.seke.paymentservice.strategies.ConfirmationRetryStrategy;
import bi.seke.schema.paymentservice.PaymentDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Service("confirmationRetryStrategy")
@AllArgsConstructor
@Log4j2
public class DefaultConfirmationRetryStrategy implements ConfirmationRetryStrategy {
    protected final PriceService priceService;
    protected final TaskScheduler taskScheduler;
    protected final Configurations configurations;
    protected final TaskRepository taskRepository;
    protected final PackageUidService packageUidService;
    protected final CustomerRepository customerRepository;


    @Override
    public void createRetryConfirmation(final PaymentDTO payment) {
        log.info("Creating Task for retrying the payment confirmation for {}", payment::getPackageUid);

        taskRepository.findByPackageUid(payment.getPackageUid())
                .or(getTaskDocumentCreator(payment))
                .filter(task -> task.getRetries() < configurations.getPriceMismatchRetries())
                .ifPresent(task -> {
                    task.setRetries(task.getRetries() + 1);
                    taskScheduler.schedule(() -> retryConfirmation(payment), getStartTime(task.getRetries()));
                    taskRepository.save(task);
                });
    }

    /**
     * Finds the price for {@link PaymentDTO#packageUid} and checks if is paid and return the overcharged amount to the customer<br>
     * otherwise returns the {@link PaymentDTO#amount} to the customer account
     *
     * @param payment paument confirmation
     */
    protected void returnPayment(final PaymentDTO payment) {
        final String customerUid = packageUidService.decodeCustomerUid(payment.getPackageUid());
        final Double difference = getDifference(payment);
        final Boolean paid = priceService.isPaid(payment.getPackageUid());

        final Double toBeReturned = paid ? Math.abs(difference) : payment.getAmount();
        log.info("Returning {} to the customer {}", toBeReturned, customerUid);

        customerRepository.findByCustomerUid(customerUid)
                .or(getCustomerCreator(customerUid))
                .ifPresent(customer -> {
                    customer.setBalance(customer.getBalance() + toBeReturned);
                    customerRepository.save(customer);
                });
    }


    /**
     * Retries the confirmation by {@link PriceService#confirmPayment(PaymentDTO)} if the price and paid amount are different<br>
     * then returns the paid amount or overcharged amount if the retries exceeds {@link Configurations#priceMismatchRetries}
     *
     * @param payment
     */
    protected void retryConfirmation(final PaymentDTO payment) {
        if (getDifference(payment) != 0) {
            log.info("Retrying the payment confirmation for {}", payment::getPackageUid);
            priceService.confirmPayment(payment);
        }

        taskRepository.findByPackageUid(payment.getPackageUid())
                .filter(task -> Objects.equals(task.getRetries(), configurations.getPriceMismatchRetries()))
                .ifPresent(task -> {
                    task.setMaxRetriesReached(true);
                    taskRepository.save(task);
                    returnPayment(payment);
                });
    }

    protected Instant getStartTime(final Integer retries) {
        final int delay = configurations.getPriceMismatchRetryDelay() * retries;
        return Instant.now().plusSeconds(delay);
    }

    private Supplier<Optional<? extends CustomerDocument>> getCustomerCreator(final String customerUid) {
        return () -> {
            final CustomerDocument customer = new CustomerDocument();
            customer.setCustomerUid(customerUid);
            customer.setBalance(0D);
            return Optional.of(customerRepository.save(customer));
        };
    }

    protected Supplier<Optional<? extends TaskDocument>> getTaskDocumentCreator(final PaymentDTO payment) {
        return () -> {
            final TaskDocument task = new TaskDocument();
            task.setPackageUid(payment.getPackageUid());
            task.setRetries(0);
            return Optional.of(taskRepository.save(task));
        };
    }

    protected Double getDifference(PaymentDTO payment) {
        return priceService.getPrice(payment.getPackageUid())
                .map(price -> price.getAmount() - payment.getAmount())
                .orElse(0d);
    }
}
