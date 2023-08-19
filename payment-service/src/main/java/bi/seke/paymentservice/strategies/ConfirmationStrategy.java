package bi.seke.paymentservice.strategies;

import bi.seke.schema.paymentservice.ConfirmationDTO;
import bi.seke.schema.paymentservice.PaymentDTO;
import bi.seke.schema.pricingservice.PriceDTO;

public interface ConfirmationStrategy {

    /**
     * Compare the price with the payment transaction if {@link PriceDTO#amount} is
     *
     * <li>the same as transaction amount, marks the confirmation status as ACCEPT and sends it to paid topic</li>
     * <li>more than the price amount, ACCEPT the delivery, sends to paid topic, and then save the transaction in OVERCHARGED,</li>
     * <li>less than Price amount, mark the payment as PAID_LESS, and save the transaction as PAID_LESS</li>
     * <li>zero, REJECT the payment</li>
     * <p>
     *
     * @param payment payment confirmation
     * @return confirmation
     */
    ConfirmationDTO createAndSendConfirmation(PaymentDTO payment);
}
