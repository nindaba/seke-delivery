package bi.seke.paymentservice.strategies;

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
     * if the confirmation has a status other than ACCEPTED, a task is creatd, to ckeck the price after ${price.mismatch.retry.delay default to 5s} exponentially for ${price.mismatch.retry.times default 3 times} <br>
     * then the amount will be stored to the clients account, or reverted if the account is set to revert over ${price.mismatch.return default false}
     *
     * @param payment payment confirmation
     */
    void createAndSendConfirmation(PaymentDTO payment);
}
