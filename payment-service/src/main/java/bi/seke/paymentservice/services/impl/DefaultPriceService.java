package bi.seke.paymentservice.services.impl;

import bi.seke.paymentservice.repositories.PriceRepository;
import bi.seke.paymentservice.services.PriceService;
import bi.seke.paymentservice.strategies.ConfirmationStrategy;
import bi.seke.schema.paymentservice.PaymentDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@AllArgsConstructor
public class DefaultPriceService implements PriceService {
    protected final PriceRepository priceRepository;
    protected final ConfirmationStrategy strategy;

    @Override
    public void confirmPayment(PaymentDTO payment) {
        log.info("Creating And Sending Confirmation for package {}", payment::getPackageUid);
        strategy.createAndSendConfirmation(payment);
    }

    @Override
    public Boolean isPaid(String packageUid) {
        return priceRepository.findById(UUID.fromString(packageUid))
                .map(price -> price.isPaid())
                .orElse(false);
    }

    @Override
    public Optional<PriceDTO> getPrice(String packageUid) {
        return priceRepository.findById(UUID.fromString(packageUid))
                .map(price -> {
                    final PriceDTO priceDTO = new PriceDTO();
                    priceDTO.setUid(price.getUid());
                    priceDTO.setPackageUid(price.getPackageUid());
                    priceDTO.setAmount(price.getAmount());
                    return priceDTO;
                });
    }

    @Override
    public void markPaid(final String packageUid) {
        priceRepository.findById(UUID.fromString(packageUid))
                .ifPresent(price -> {
                    price.setPaid(true);
                    priceRepository.save(price);
                });
    }
}
