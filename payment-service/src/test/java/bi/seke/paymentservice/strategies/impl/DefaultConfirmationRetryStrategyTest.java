package bi.seke.paymentservice.strategies.impl;

import bi.seke.paymentservice.configurations.Configurations;
import bi.seke.paymentservice.documents.CustomerDocument;
import bi.seke.paymentservice.documents.TaskDocument;
import bi.seke.paymentservice.repositories.CustomerRepository;
import bi.seke.paymentservice.repositories.TaskRepository;
import bi.seke.paymentservice.services.PackageUidService;
import bi.seke.paymentservice.services.PriceService;
import bi.seke.schema.paymentservice.PaymentDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultConfirmationRetryStrategyTest {
    static PaymentDTO PAYMENT = new PaymentDTO();
    static PriceDTO PRICE = new PriceDTO();
    @Spy
    TaskDocument TASK = new TaskDocument();
    @Spy
    CustomerDocument CUSTOMER = new CustomerDocument();
    @Spy
    PriceService priceService;
    @Spy
    TaskScheduler taskScheduler;
    @Spy
    Configurations configurations;
    @Spy
    TaskRepository taskRepository;
    @Spy
    PackageUidService packageUidService;
    @Spy
    CustomerRepository customerRepository;

    @InjectMocks
    @Spy
    DefaultConfirmationRetryStrategy strategy;

    @BeforeAll
    void setUp() {
        TASK.setRetries(0);
        PAYMENT.setPackageUid("some");
        PAYMENT.setAmount(20D);
        CUSTOMER.setBalance(0D);
        PRICE.setAmount(10D);
    }

    @Test
    void createRetryConfirmation() {
        when(taskRepository.findByPackageUid(PAYMENT.getPackageUid())).thenReturn(Optional.of(TASK));
        strategy.createRetryConfirmation(PAYMENT);

        verify(configurations).getPriceMismatchRetries();
        verify(configurations).getPriceMismatchRetryDelay();
        verify(taskRepository).save(TASK);
        verify(strategy).getStartTime(1);
        verify(taskScheduler).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void returnPayment() {
        String CUSTOMER_UID = "customer";
        CUSTOMER.setBalance(0D);
        when(packageUidService.decodeCustomerUid(PAYMENT.getPackageUid())).thenReturn(CUSTOMER_UID);
        when(priceService.isPaid(PAYMENT.getPackageUid())).thenReturn(Boolean.FALSE);
        when(customerRepository.findByCustomerUid(CUSTOMER_UID)).thenReturn(Optional.ofNullable(CUSTOMER));
        strategy.returnPayment(PAYMENT);
        verify(customerRepository).save(CUSTOMER);
        assertEquals(PAYMENT.getAmount(), CUSTOMER.getBalance());

        CUSTOMER.setBalance(0D);
        when(priceService.getPrice(PAYMENT.getPackageUid())).thenReturn(Optional.ofNullable(PRICE));
        when(priceService.isPaid(PAYMENT.getPackageUid())).thenReturn(Boolean.TRUE);
        strategy.returnPayment(PAYMENT);
        assertEquals(Double.valueOf(PAYMENT.getAmount() - PRICE.getAmount()), CUSTOMER.getBalance());
    }

    @Test
    void retryConfirmation() {
        TASK.setRetries(1);
        int FIRST_RETRY_ONLY = 1;
        when(priceService.getPrice(PAYMENT.getPackageUid())).thenReturn(Optional.ofNullable(PRICE));
        when(taskRepository.findByPackageUid(PAYMENT.getPackageUid())).thenReturn(Optional.ofNullable(TASK));
        when(configurations.getPriceMismatchRetries()).thenReturn(3);
        when(customerRepository.save(any())).thenReturn(CUSTOMER);
        doNothing().when(priceService).confirmPayment(PAYMENT);
        strategy.retryConfirmation(PAYMENT);
        verify(priceService, times(FIRST_RETRY_ONLY)).confirmPayment(PAYMENT);

        PRICE.setAmount(PAYMENT.getAmount());
        strategy.retryConfirmation(PAYMENT);
        verify(priceService, times(FIRST_RETRY_ONLY)).confirmPayment(PAYMENT);

        TASK.setRetries(3);
        strategy.retryConfirmation(PAYMENT);
        verify(TASK).setMaxRetriesReached(true);

    }
}
