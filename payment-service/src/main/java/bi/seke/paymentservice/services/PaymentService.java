package bi.seke.paymentservice.services;

import bi.seke.schema.paymentservice.PaymentDTO;

public interface PaymentService {
    /**
     * Confirms and write the confirmation to paid topic
     * <p>
     * if the confirmation has a status other than ACCEPTED, a task is creatd, to ckeck the price after ${price.mismatch.retry.delay default to 5s} exponentially for ${price.mismatch.retry.times default 3 times} <br>
     * then the amount will be stored to the clients account, or reverted if the account is set to revert over ${price.mismatch.return default false}
     *
     * @param payment payment transaction
     */
    void confirmPayment(PaymentDTO payment);
}
