package bi.seke.paymentservice.strategies.impl;

import bi.seke.paymentservice.configurations.Configurations;
import bi.seke.paymentservice.documents.CustomerDocument;
import bi.seke.paymentservice.documents.TaskDocument;
import bi.seke.paymentservice.repositories.CustomerRepository;
import bi.seke.paymentservice.repositories.TaskRepository;
import bi.seke.paymentservice.services.PackageUidService;
import bi.seke.paymentservice.services.PriceService;
import bi.seke.paymentservice.strategies.ConfirmationRetryStrategy;
import bi.seke.paymentservice.strategies.ConfirmationStrategy;
import bi.seke.schema.paymentservice.ConfirmationStatus;
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
    protected final ConfirmationStrategy confirmationStrategy;
    protected final TaskScheduler psTaskScheduler;
    protected final Configurations configurations;
    protected final TaskRepository taskRepository;
    protected final PackageUidService packageUidService;
    protected final CustomerRepository customerRepository;

    @Override
    public void startConfirmationRetryTasks(PaymentDTO payment) {
        taskRepository.findByPackageUid(payment.getPackageUid()).ifPresent(task -> {
            task.setRetries(0);
            task.setMaxRetriesReached(false);
            taskRepository.save(task);
        });

        createRetryConfirmation(payment);
    }

    @Override
    public void createRetryConfirmation(final PaymentDTO payment) {
        log.debug("Creating Task for retrying the payment confirmation for {}", payment::getPackageUid);

        taskRepository.findByPackageUid(payment.getPackageUid()).or(getTaskDocumentCreator(payment)).filter(task -> !task.isMaxRetriesReached()).ifPresent(task -> psTaskScheduler.schedule(() -> retryConfirmation(payment, task), getStartTime(task.getRetries())));
    }

    /**
     * Finds the price for {@link PaymentDTO#packageUid} and checks if is paid then checks if {@link PaymentDTO#amount} > 0 and return the overcharged amount to the customer<br>
     * otherwise returns the {@link PaymentDTO#amount} to the customer account
     *
     * @param payment paument confirmation
     */
    protected void returnPayment(final PaymentDTO payment) {
        final String customerUid = packageUidService.decodeCustomerUid(payment.getPackageUid());
        final Double difference = getDifference(payment);
        final Boolean paid = priceService.isPaid(payment.getPackageUid());
        final Double toBeReturned = paid && payment.getAmount() > 0 ? Math.abs(difference) : payment.getAmount();

        customerRepository.findByCustomerUid(customerUid).or(getCustomerCreator(customerUid)).filter(customer -> toBeReturned > 0).ifPresent(customer -> {
            log.debug("Returning {} to the customer {}", toBeReturned, customerUid);
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
    protected void retryConfirmation(final PaymentDTO payment, final TaskDocument task) {
        if (Objects.equals(task.getRetries(), configurations.getPriceMismatchRetries())) {
            task.setMaxRetriesReached(true);
            returnPayment(payment);
        }

        task.setRetries(task.getRetries() + 1);
        taskRepository.save(task);

        if (getDifference(payment) != 0 && !task.isMaxRetriesReached()) {
            log.debug("Retrying the payment confirmation for {}, pass {}", payment.getPackageUid(), task.getRetries());

            Optional.of(confirmationStrategy.createAndSendConfirmation(payment)).filter(confirmation -> confirmation.getStatus() != ConfirmationStatus.ACCEPTED).ifPresent(confirmation -> {
                log.debug("Payment for {} will be retried as, {}", payment.getPackageUid(), confirmation.getMessage());
                createRetryConfirmation(payment);
            });
        }
    }

    protected Instant getStartTime(final Integer retries) {
        final int delay = configurations.getPriceMismatchRetryDelay() * (retries + 1);
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
        return priceService.getPrice(payment.getPackageUid()).map(price -> payment.getAmount() - price.getAmount()).orElse(0d);
    }
}
