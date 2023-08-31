package bi.seke.paymentservice.services.impl;

import bi.seke.paymentservice.documents.PriceDocument;
import bi.seke.paymentservice.repositories.PriceRepository;
import bi.seke.paymentservice.services.PriceService;
import bi.seke.schema.pricingservice.PriceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@AllArgsConstructor
public class DefaultPriceService implements PriceService {
    protected final PriceRepository priceRepository;

    @Override
    public Boolean isPaid(String packageUid) {
        return priceRepository.findByPackageUid(packageUid)
                .map(PriceDocument::isPaid)
                .orElse(false);
    }

    @Override
    public Optional<PriceDTO> getPrice(String packageUid) {
        return priceRepository.findByPackageUid(packageUid)
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
        priceRepository.findByPackageUid(packageUid)
                .ifPresent(price -> {
                    price.setPaid(true);
                    priceRepository.save(price);
                });
    }

    @Override
    public void reset(String packageUid) {
        priceRepository.findByPackageUid(packageUid)
                .ifPresent(price -> {
                    price.setPaid(false);
                    priceRepository.save(price);
                });
    }
}
