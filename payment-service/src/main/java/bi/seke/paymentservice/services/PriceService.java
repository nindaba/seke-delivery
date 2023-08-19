package bi.seke.paymentservice.services;

import bi.seke.schema.pricingservice.PriceDTO;

import java.util.Optional;

public interface PriceService {
    /**
     * Finds a price with package uid and check if it is marked as paid
     *
     * @param packageUid package uid
     * @return true if the price is marked as paid
     */
    Boolean isPaid(String packageUid);

    /**
     * Finds Price bu package uid
     *
     * @param packageUid package uid
     * @return price
     */
    Optional<PriceDTO> getPrice(String packageUid);

    /**
     * Finds a package price with package uid, then change the flag of is paid to true
     *
     * @param packageUid package uid
     */
    void markPaid(String packageUid);

    /**
     * Resets {@link #markPaid(String)}
     *
     * @param packageUid
     */
    void reset(String packageUid);
}
