package bi.seke.paymentservice.strategies.impl;

import bi.seke.paymentservice.configurations.Configurations;
import bi.seke.paymentservice.services.PriceService;
import bi.seke.paymentservice.strategies.ConfirmationRetryStrategy;
import bi.seke.schema.paymentservice.ConfirmationDTO;
import bi.seke.schema.paymentservice.ConfirmationStatus;
import bi.seke.schema.paymentservice.PaymentDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static bi.seke.schema.paymentservice.ConfirmationStatus.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultConfirmationStrategyTest {
    private static final ConfirmationDTO CONFIRMATION = new ConfirmationDTO();
    static PaymentDTO PAYMENT = new PaymentDTO();
    static PriceDTO PRICE = new PriceDTO();

    static Map<Integer, ConfirmationStatus> CONFIRMATION_STATUSES = Map.of(
            0, ACCEPTED,
            -1, PAYED_LESS,
            1, OVERCHARGED);
    static List<ConfirmationStatus> ACCEPTED_CONFIRMATION_STATUSES = List.of(
            ACCEPTED,
            PAYED_LESS,
            OVERCHARGED);

    @InjectMocks
    DefaultConfirmationStrategy strategy;

    @Spy
    PriceService priceService;
    @Mock
    KafkaTemplateTest template;
    @Spy
    Configurations configurations;
    @Spy
    ConfirmationRetryStrategy retryStrategy;

    @BeforeAll
    void setUp() {
        PAYMENT.setPackageUid("some");
    }

    @Test
    void createAndSendConfirmation() {
        String TOPIC = "Topic";
        PAYMENT.setAmount(20D);
        PRICE.setAmount(20D);
        CONFIRMATION.setStatus(ACCEPTED);
        setMessage(CONFIRMATION);
        when(configurations.getAcceptedConfirmationStatuses()).thenReturn(ACCEPTED_CONFIRMATION_STATUSES);
        when(priceService.getPrice(PAYMENT.getPackageUid())).thenReturn(Optional.ofNullable(PRICE));

        when(configurations.getPaymentConfirmationStatuses()).thenReturn(CONFIRMATION_STATUSES);
        doNothing().when(priceService).markPaid(PAYMENT.getPackageUid());
        when(configurations.getPaidTopicName()).thenReturn(TOPIC);
        when(template.send(TOPIC, PAYMENT.getPackageUid(), CONFIRMATION)).thenReturn(new CompletableFuture<>());
        strategy.createAndSendConfirmation(PAYMENT);

        PAYMENT.setAmount(20D);
        PRICE.setAmount(10D);
        CONFIRMATION.setStatus(OVERCHARGED);
        setMessage(CONFIRMATION);
        when(template.send(TOPIC, PAYMENT.getPackageUid(), CONFIRMATION)).thenReturn(new CompletableFuture<>());
        doNothing().when(retryStrategy).createRetryConfirmation(PAYMENT);
        strategy.createAndSendConfirmation(PAYMENT);


    }

    @Test
    void getPaymentStatus() {
        ConfirmationStatus ACTUAL;
        PAYMENT.setAmount(20D);
        PRICE.setAmount(20D);
        when(configurations.getPaymentConfirmationStatuses()).thenReturn(CONFIRMATION_STATUSES);

        ACTUAL = strategy.getPaymentStatus(PAYMENT).apply(PRICE);
        assertEquals(ACTUAL, ACCEPTED);

        PAYMENT.setAmount(20D);
        PRICE.setAmount(10D);
        ACTUAL = strategy.getPaymentStatus(PAYMENT).apply(PRICE);
        assertEquals(ACTUAL, OVERCHARGED);

        PAYMENT.setAmount(0D);
        PRICE.setAmount(10D);
        ACTUAL = strategy.getPaymentStatus(PAYMENT).apply(PRICE);
        assertEquals(ACTUAL, PAYED_LESS);
    }

    private void setMessage(final ConfirmationDTO confirmation) {
        if (confirmation.getStatus() != ConfirmationStatus.ACCEPTED) {
            confirmation.setMessage("This confirmation will be repeated since the paid amount does not match the price ⚠️");
        } else {
            confirmation.setMessage("Confirmed ✅");
        }
    }
}
