package bi.seke.paymentservice.strategies;

import bi.seke.schema.paymentservice.PaymentDTO;

public interface ConfirmationRetryStrategy {

    /**
     * creates a task, to check the price after ${price.mismatch.retry.delay default to 5s} exponentially for ${price.mismatch.retry.times default 3 times} <br>
     * then the amount will be stored to the clients account, or reverted if the account is set to revert over ${price.mismatch.return default false}
     *
     * @param payment the payment transaction
     */
    void createRetryConfirmation(PaymentDTO payment);

    /**
     * If there is a task related to the {@link PaymentDTO#packageUid}, it resets the number of reties to 0<br>
     * and starts {@link #createRetryConfirmation(PaymentDTO)}
     *
     * @param payment
     */
    void startConfirmationRetryTasks(PaymentDTO payment);
}
