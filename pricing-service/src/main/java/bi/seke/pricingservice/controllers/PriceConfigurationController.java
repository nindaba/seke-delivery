package bi.seke.pricingservice.controllers;

import bi.seke.pricingservice.entities.PriceConfigurationEntity;
import bi.seke.pricingservice.services.PriceConfigurationService;
import bi.seke.schema.deliveryservice.PackageDTO;
import bi.seke.schema.pricingservice.PriceDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(value = "/price-settings")
@AllArgsConstructor
public class PriceConfigurationController {
    protected final PriceConfigurationService service;


    @GetMapping
    public Collection<PriceConfigurationEntity> getAllPriceConfigurations(
            @RequestParam(required = false) final Optional<String> target) {

        return target.map(service::getAllPriceConfigurations)
                .orElse(service.getAllPriceConfigurations());
    }

    @GetMapping("/{uid}")
    public ResponseEntity<PriceConfigurationEntity> getPriceConfigurationByUid(
            @PathVariable(required = false) final Optional<String> uid) {

        return ResponseEntity.of(uid.flatMap(service::getPriceConfiguration));
    }

    @DeleteMapping
    public void deletePriceConfigurations(@RequestParam final Collection<String> uids) {
        service.deletePriceConfigurations(uids);
    }

    @PostMapping
    public ResponseEntity<PriceConfigurationEntity> savePriceConfiguration(@RequestBody final PriceConfigurationEntity priceConfiguration) {
        return ResponseEntity.ok(service.savePriceConfiguration(priceConfiguration));
    }

    @PostMapping("/calculate")
    public ResponseEntity<PriceDTO> calculatePrice(@RequestBody final PackageDTO packag) {
        return ResponseEntity.ok(service.calculatePackagePrice(packag));
    }
}
