package bi.seke.paymentservice.services;

import bi.seke.schema.pricingservice.PriceDTO;

import java.util.Optional;

public interface PriceService {
    /**
     * Finds Price bu package uid
     *
     * @param packageUid package uid
     * @return price
     */
    Optional<PriceDTO> getPrice(String packageUid);
}
